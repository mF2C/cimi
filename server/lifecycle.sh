#!/bin/bash -x

#
# Example script to exercise the full lifecycle of one CIMI resource.
#

BASE_URI=https://localhost/api/

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
               2>&1 | grep -i location: | cut -d ' ' -f 3`

LOCATION="${BASE_URI}${RELATIVE_URL}"
LOCATION=`echo ${LOCATION} | tr '\r' ' '`

echo "DOCUMENT URL: " ${LOCATION}

echo "GET CREATED DOCUMENT"
curl -s -XGET -k \
  -H 'content-type: application/json' \
  -H 'slipstream-authn-info: internal ADMIN' \
  ${LOCATION}

echo "UPDATE CREATE DOCUMENT WITH NEW NAME"
curl -s -XPUT -k \
  -H 'content-type: application/json' \
  -H 'slipstream-authn-info: internal ADMIN' \
  -d '{"name": "UPDATED NAME", "max_cpu_usage": 1000}' \
  ${LOCATION}

echo "GET UPDATED DOCUMENT"
curl -s -XGET -k \
  -H 'content-type: application/json' \
  -H 'slipstream-authn-info: internal ADMIN' \
  ${LOCATION}

echo "QUERY THE DOCUMENTS"
curl -s -XGET -k \
  -H 'content-type: application/json' \
  -H 'slipstream-authn-info: internal ADMIN' \
  ${BASE_URI}sharing-model

echo "QUERY THE DOCUMENTS WITH FILTER (EMPTY)"
curl -s -XGET -k \
  -H 'content-type: application/json' \
  -H 'slipstream-authn-info: internal ADMIN' \
  ${BASE_URI}sharing-model?'$filter=max_storage_usage<200%20and%20gps_allowed=false'

echo "QUERY THE DOCUMENTS WITH FILTER (NOT EMPTY)"
curl -s -XGET -k \
  -H 'content-type: application/json' \
  -H 'slipstream-authn-info: internal ADMIN' \
  ${BASE_URI}sharing-model?'$filter=max_storage_usage>200%20and%20gps_allowed=false'

echo "DELETE THE DOCUMENT"
curl -s -XDELETE -k \
  -H 'content-type: application/json' \
  -H 'slipstream-authn-info: internal ADMIN' \
  ${LOCATION}

echo "ENSURE DOCUMENT IS DELETED"
curl -s -XGET -k \
  -H 'content-type: application/json' \
  -H 'slipstream-authn-info: internal ADMIN' \
  ${LOCATION}
