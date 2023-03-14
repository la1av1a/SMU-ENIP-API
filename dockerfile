FROM node:19.7.0
MAINTAINER la1av1a <zldqkrktqk@gmail.com>
COPY ./ ./
RUN npm install
EXPOSE 8080

CMD ["node", "index.js"]