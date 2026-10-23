# demo

每个子目录是一个应用(kustomize),自带 `namespace.yaml`。
`envs/<env>.yaml` 是部署标记:有该文件才会部署到对应环境,删除则从该环境下线。
ApplicationSet 扫描 `demo/*/envs/<env>.yaml` 自动生成 Application。
