FROM alpine
MAINTAINER Cristovao Cordeiro <cris@sixsq.com>

RUN apk update && apk add jq curl openssl 

ADD cimi-tests.sh /root/run.sh
RUN chmod +x /root/run.sh

CMD ["sh", "/root/run.sh" ]
