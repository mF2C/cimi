#!/bin/bash -x

#
# Example script to exercise the full lifecycle of one CIMI resource.
#

BASE_URI=http://localhost:5555/api/

cat > sharing-model-example.json <<'EOF'
{
    "description": "example of a sharing model resource instance",
    "max_apps": 3,
    "gps_allowed": false,
    "max_cpu_usage": 1,
    "max_memory_usage": 1024,
    "max_storage_usage": 500,
    "max_bandwidth_usage": 20,
    "battery_limit": 10
}
EOF

RELATIVE_URL=`curl -XPOST -k \
              -H 'content-type: application/json' \
              -H 'slipstream-authn-info: internal ADMIN' \
              -d @sharing-model-example.json \
               ${BASE_URI}sharing-model \
               -s -v \
               2>&1 | grep Location: | cut -d ' ' -f 3`

LOCATION="${BASE_URI}${RELATIVE_URL}"
LOCATION=`echo ${LOCATION} | tr '\r' ' '`

echo "DOCUMENT URL: " ${LOCATION}

echo "GET CREATED DOCUMENT"
curl -XGET -k \
  -H 'content-type: application/json' \
  -H 'slipstream-authn-info: internal ADMIN' \
  ${LOCATION}

echo "UPDATE CREATE DOCUMENT WITH NEW NAME"
curl -XPUT -k \
  -H 'content-type: application/json' \
  -H 'slipstream-authn-info: internal ADMIN' \
  -d '{"name": "UPDATED NAME", "max_cpu_usage": 1000}' \
  ${LOCATION}

echo "GET UPDATED DOCUMENT"
curl -XGET -k \
  -H 'content-type: application/json' \
  -H 'slipstream-authn-info: internal ADMIN' \
  ${LOCATION}

echo "QUERY THE DOCUMENTS"
curl -XGET -k \
  -H 'content-type: application/json' \
  -H 'slipstream-authn-info: internal ADMIN' \
  ${BASE_URI}sharing-model

echo "DELETE THE DOCUMENT"
curl -XDELETE -k \
  -H 'content-type: application/json' \
  -H 'slipstream-authn-info: internal ADMIN' \
  ${LOCATION}

echo "ENSURE DOCUMENT IS DELETED"
curl -XGET -k \
  -H 'content-type: application/json' \
  -H 'slipstream-authn-info: internal ADMIN' \
  ${LOCATION}
