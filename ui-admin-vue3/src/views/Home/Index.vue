<template>
  <div>
    <el-card shadow="never">
      <el-skeleton :loading="loading" animated>
        <el-row :gutter="16" justify="space-between">
          <el-col :xl="24" :lg="24" :md="24" :sm="24" :xs="24">
            <div class="flex items-center">
              <el-avatar :src="avatar" :size="70" class="mr-16px">
                <img src="@/assets/imgs/avatar.gif" alt="" />
              </el-avatar>
              <div>
                <div class="text-20px">
                  {{ t('workplace.welcome') }} {{ username }} {{ t('workplace.happyDay') }}
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </el-skeleton>
    </el-card>
  </div>

  <el-row class="mt-8px" :gutter="8" justify="space-between">
    <el-col :xl="16" :lg="16" :md="24" :sm="24" :xs="24" class="mb-8px">
      <el-card shadow="never" class="mt-8px">
        <el-skeleton :loading="loading" animated>
          <el-row :gutter="20" justify="space-between">
            <el-col :xl="24" :lg="24" :md="24" :sm="24" :xs="24">
              <el-card shadow="hover" class="mb-8px">
                <el-skeleton :loading="loading" animated>
                  <Echart :options="barOptionsData" :height="280" />
                </el-skeleton>
              </el-card>
            </el-col>
          </el-row>
        </el-skeleton>
      </el-card>
    </el-col>
    <el-col :xl="8" :lg="8" :md="24" :sm="24" :xs="24" class="mb-8px">
      <el-card shadow="never">
        <template #header>
          <div class="flex items-center justify-between">
            <el-tabs v-model="activeNoticeTab" @tab-change="handleTabChange" class="notice-tabs">
              <el-tab-pane label="通知" name="notice" />
              <el-tab-pane label="公告" name="announcement" />
            </el-tabs>
            <el-link type="primary" :underline="false">{{ t('action.more') }}</el-link>
          </div>
        </template>
        <el-skeleton :loading="loading" animated>
          <div v-for="(item, index) in notice" :key="`dynamics-${index}`">
            <div class="flex items-center">
              <el-avatar :src="avatar" :size="35" class="mr-16px">
                <img src="@/assets/imgs/avatar.gif" alt="" />
              </el-avatar>
              <div>
                <div class="text-14px"> {{ item.title }} </div>
                <div class="mt-16px text-12px text-gray-400">
                  {{ formatTime(item.date, 'yyyy-MM-dd') }}
                </div>
              </div>
            </div>
            <el-divider />
          </div>
        </el-skeleton>
      </el-card>
    </el-col>
  </el-row>
</template>
<script lang="ts" setup>
import { set } from 'lodash-es'
import { EChartsOption } from 'echarts'
import { formatTime } from '@/utils'

import { useUserStore } from '@/store/modules/user'
// import { useWatermark } from '@/hooks/web/useWatermark'
import type { Notice } from './types'
import { getNoticePage } from '@/api/system/notice'
import { barOptions } from './echarts-data'

defineOptions({ name: 'Index' })

const { t } = useI18n()
const userStore = useUserStore()
// const { setWatermark } = useWatermark()
const loading = ref(true)
const avatar = userStore.getUser.avatar
const username = userStore.getUser.nickname

// 标签页相关
const activeNoticeTab = ref('notice')

// 获取通知公告
let notice = reactive<Notice[]>([])
const getNotice = async () => {
  try {
    // 根据当前标签页确定公告类型：通知(1) 或 公告(2)
    const type = activeNoticeTab.value === 'notice' ? 1 : 2
    const res = await getNoticePage({ pageNo: 1, pageSize: 5, status: 0, type })
    if (res && res.list) {
      const data = res.list.map((item: any) => ({
        title: item.title,
        type: item.type === 1 ? '通知' : '公告',
        keys: [],
        date: item.createTime
      }))
      notice.splice(0, notice.length, ...data)
    }
  } catch (error) {
    console.error('获取通知公告失败:', error)
  }
}

// 标签页切换处理
const handleTabChange = () => {
  getNotice()
}

const barOptionsData = reactive<EChartsOption>(barOptions) as EChartsOption

// 周活跃量
const getWeeklyUserActivity = async () => {
  const data = [
    { value: 13253, name: 'analysis.monday' },
    { value: 34235, name: 'analysis.tuesday' },
    { value: 26321, name: 'analysis.wednesday' },
    { value: 12340, name: 'analysis.thursday' },
    { value: 24643, name: 'analysis.friday' },
    { value: 1322, name: 'analysis.saturday' },
    { value: 1324, name: 'analysis.sunday' }
  ]
  set(
    barOptionsData,
    'xAxis.data',
    data.map((v) => t(v.name))
  )
  set(barOptionsData, 'series', [
    {
      name: t('analysis.activeQuantity'),
      data: data.map((v) => v.value),
      type: 'bar'
    }
  ])
}

const getAllApi = async () => {
  await Promise.all([getNotice(), getWeeklyUserActivity()])
  loading.value = false
}

getAllApi()
</script>

<style lang="scss" scoped>
:deep(.notice-tabs) {
  .el-tabs__header {
    margin: 0;
  }
  .el-tabs__nav-wrap::after {
    display: none;
  }
  .el-tabs__item {
    height: 32px;
    line-height: 32px;
  }
}
</style>
