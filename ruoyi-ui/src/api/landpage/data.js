import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

// 查询落地页列表
export function getlist() {
  return request({
    url: '/land_page/list',
    method: 'get',
  })
}

// 查询用户配置列表
export function configPage(data) {
  return request({
    url: '/land_page/config',
    method: 'post',
    data: parseStrEmpty(data)
  })
}

// 增加落地页
export function addpage(data) {
  return request({
    url: '/land_page/add_page',
    method: 'post',
    data: parseStrEmpty(data)
  })
}

// 增加落地页
export function addconfig(data) {
  return request({
    url: '/land_page/add_config',
    method: 'post',
    data: parseStrEmpty(data)
  })
}

// 删除用户配置页面
export function deleteConfig(data) {
  return request({
    url: '/land_page/delete_config',
    method: 'post',
    data: parseStrEmpty(data)
  })
}

