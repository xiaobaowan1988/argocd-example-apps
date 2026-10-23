# demo

每个子目录是一个应用(kustomize)。命名空间由 ApplicationSet 的 `CreateNamespace=true` 创建,不属于应用管理。
`envs/<env>.yaml` 是部署标记:有该文件才会部署到对应环境,删除则从该环境下线(Application 及其资源被清理,命名空间保留)。
ApplicationSet 扫描 `demo/*/envs/<env>.yaml` 自动生成 Application。
