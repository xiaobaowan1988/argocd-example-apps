# 在集群里由 kaniko 构建:源码编译成静态二进制,再拷进 scratch 镜像。
# VERSION 由流水线通过 --build-arg 传入(运行编号),编进二进制,服务会在响应里返回它。
FROM mirror.gcr.io/library/golang:1.24-alpine AS build
ARG VERSION=dev
WORKDIR /src
COPY . .
RUN CGO_ENABLED=0 go build -trimpath -ldflags="-s -w -X main.version=${VERSION}" -o /out/api .

FROM scratch
COPY --from=build /out/api /api
USER 65532:65532
ENTRYPOINT ["/api"]
