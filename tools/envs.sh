#!/bin/bash
# 管理应用的部署标记:demo/<应用>/envs/<环境>.yaml 存在 = 部署到该环境(环境名取自文件名,如 dev3)
# 用法:
#   tools/envs.sh enable  <应用> <环境>...     例: enable app-a dev1 dev2
#   tools/envs.sh enable  <应用> dev{1..12}    批量(bash 展开)
#   tools/envs.sh disable <应用> <环境>...
#   tools/envs.sh list    [应用]               列出各应用部署在哪些环境
set -euo pipefail
cd "$(dirname "$0")/.."
cmd=${1:?用法见脚本头部}; shift
case $cmd in
  enable)
    app=${1:?缺少应用名}; shift; [ -d demo/$app ] || { echo "demo/$app 不存在" >&2; exit 1; }
    mkdir -p demo/$app/envs
    for e in "$@"; do printf '# 部署标记:文件存在即部署到 %s\nenabled: true\n' "$e" > demo/$app/envs/$e.yaml; done ;;
  disable)
    app=${1:?缺少应用名}; shift
    for e in "$@"; do rm -f demo/$app/envs/$e.yaml; done ;;
  list)
    for d in demo/${1:-*}/; do a=$(basename "$d"); [ -d "$d/envs" ] || continue
      printf '%-12s %s\n' "$a" "$(ls "$d/envs" | sed 's/\.yaml$//' | sort -V | tr '\n' ' ')"; done ;;
  *) echo "未知命令 $cmd" >&2; exit 1 ;;
esac
