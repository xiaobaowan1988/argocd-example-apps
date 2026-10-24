# demo

每个子目录是一个 kustomize 应用(`app-a`、`app-b`、`app-c`、`web`),**每个应用只有一份清单,不区分环境**。
"部署到哪些环境、副本数多少、镜像是什么"不在这个仓库里,由 myk8s 仓库里的 deployer(MySQL 存期望状态)管理,
deployer 通过 Argo CD Application 的 `source.kustomize.replicas/images` 把环境差异传给 Argo CD。
命名空间由 Argo CD 的 `CreateNamespace=true` 创建(`<环境>-env-<应用>`),不属于应用管理。
