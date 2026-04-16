<template>
  <div>
    <el-card shadow="never">
      <el-skeleton :loading="loading" animated>
        <div class="flex items-center">
          <el-avatar :src="avatar" :size="70" class="mr-16px">
            <img src="@/assets/imgs/avatar.gif" alt="" />
          </el-avatar>
          <div>
            <div class="text-20px"> {{ greeting }}，{{ username }}！ </div>
            <div class="mt-8px text-14px text-gray-400"> {{ deptName }} · {{ roleName }} </div>
          </div>
        </div>
      </el-skeleton>
    </el-card>
  </div>

  <el-row class="mt-8px" :gutter="8">
    <el-col :xl="6" :lg="6" :md="12" :sm="12" :xs="24" class="mb-8px">
      <el-card shadow="hover" class="cursor-pointer" @click="handleCardClick('message')">
        <el-skeleton :loading="loading" :rows="2" animated>
          <div class="flex items-center justify-between">
            <div
              class="p-16px inline-block rounded-6px"
              style="background: rgba(54, 163, 247, 0.1); color: #36a3f7"
            >
              <Icon :size="40" icon="svg-icon:message" />
            </div>
            <div class="flex flex-col justify-between text-right">
              <div class="text-16px text-gray-500">未读消息</div>
              <CountTo
                :duration="2600"
                :end-val="statsData.unreadMessage"
                :start-val="0"
                class="text-20px font-700"
              />
            </div>
          </div>
        </el-skeleton>
      </el-card>
    </el-col>

    <el-col :xl="6" :lg="6" :md="12" :sm="12" :xs="24" class="mb-8px">
      <el-card shadow="hover" class="cursor-pointer" @click="handleCardClick('todo')">
        <el-skeleton :loading="loading" :rows="2" animated>
          <div class="flex items-center justify-between">
            <div
              class="p-16px inline-block rounded-6px"
              style="background: rgba(64, 201, 198, 0.1); color: #40c9c6"
            >
              <Icon :size="40" icon="ep:document-checked" />
            </div>
            <div class="flex flex-col justify-between text-right">
              <div class="text-16px text-gray-500">待办任务</div>
              <CountTo
                :duration="2600"
                :end-val="statsData.todoTask"
                :start-val="0"
                class="text-20px font-700"
              />
            </div>
          </div>
        </el-skeleton>
      </el-card>
    </el-col>

    <el-col :xl="6" :lg="6" :md="12" :sm="12" :xs="24" class="mb-8px">
      <el-card shadow="hover" class="cursor-pointer" @click="handleCardClick('process')">
        <el-skeleton :loading="loading" :rows="2" animated>
          <div class="flex items-center justify-between">
            <div
              class="p-16px inline-block rounded-6px"
              style="background: rgba(244, 81, 108, 0.1); color: #f4516c"
            >
              <Icon :size="40" icon="ep:files" />
            </div>
            <div class="flex flex-col justify-between text-right">
              <div class="text-16px text-gray-500">我的流程</div>
              <CountTo
                :duration="2600"
                :end-val="statsData.myProcess"
                :start-val="0"
                class="text-20px font-700"
              />
            </div>
          </div>
        </el-skeleton>
      </el-card>
    </el-col>

    <el-col :xl="6" :lg="6" :md="12" :sm="12" :xs="24" class="mb-8px">
      <el-card shadow="hover" class="cursor-pointer" @click="handleCardClick('copy')">
        <el-skeleton :loading="loading" :rows="2" animated>
          <div class="flex items-center justify-between">
            <div
              class="p-16px inline-block rounded-6px"
              style="background: rgba(52, 191, 163, 0.1); color: #34bfa3"
            >
              <Icon :size="40" icon="ep:share" />
            </div>
            <div class="flex flex-col justify-between text-right">
              <div class="text-16px text-gray-500">抄送给我</div>
              <CountTo
                :duration="2600"
                :end-val="statsData.copyProcess"
                :start-val="0"
                class="text-20px font-700"
              />
            </div>
          </div>
        </el-skeleton>
      </el-card>
    </el-col>
  </el-row>

  <el-row class="mt-8px" :gutter="8" justify="space-between">
    <el-col :xl="16" :lg="16" :md="24" :sm="24" :xs="24" class="mb-8px">
      <el-card shadow="never">
        <template #header>
          <div class="flex items-center justify-between">
            <span class="text-16px font-700">我的待办</span>
            <el-button link type="primary" @click="handleMoreTodo">查看更多</el-button>
          </div>
        </template>
        <el-skeleton :loading="loading" animated>
          <el-table :data="todoList" stripe style="width: 100%" :show-header="true" size="small">
            <el-table-column
              label="流程"
              prop="processInstance.name"
              min-width="140"
              show-overflow-tooltip
            />
            <el-table-column label="当前任务" prop="name" min-width="120" show-overflow-tooltip />
            <el-table-column
              label="发起人"
              prop="processInstance.startUser.nickname"
              width="90"
              align="center"
            />
            <el-table-column label="发起时间" width="160" align="center">
              <template #default="scope">
                {{ formatDate(scope.row.processInstance?.createTime) }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="80" align="center">
              <template #default="scope">
                <el-button link type="primary" @click="handleAudit(scope.row)">办理</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-empty
            v-if="!loading && todoList.length === 0"
            description="暂无待办任务"
            :image-size="80"
          />
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
            <el-link type="primary" :underline="false" @click="handleMoreNotice">更多</el-link>
          </div>
        </template>
        <el-skeleton :loading="loading" animated>
          <div v-for="(item, index) in notice" :key="`notice-${index}`">
            <div class="flex items-center py-4px">
              <el-tag
                :type="item.type === '通知' ? 'primary' : 'warning'"
                size="small"
                class="mr-8px"
                style="flex-shrink: 0"
              >
                {{ item.type }}
              </el-tag>
              <div class="flex-1 overflow-hidden">
                <div class="text-14px truncate"> {{ item.title }} </div>
                <div class="mt-4px text-12px text-gray-400">
                  {{ formatDate(item.date as any, 'YYYY-MM-DD') }}
                </div>
              </div>
            </div>
            <el-divider v-if="index < notice.length - 1" class="!my-8px" />
          </div>
          <el-empty
            v-if="!loading && notice.length === 0"
            description="暂无通知公告"
            :image-size="80"
          />
        </el-skeleton>
      </el-card>
    </el-col>
  </el-row>

  <el-row class="mt-8px" :gutter="8" justify="space-between">
    <el-col :xl="16" :lg="16" :md="24" :sm="24" :xs="24" class="mb-8px">
      <el-card shadow="never">
        <template #header>
          <div class="flex items-center justify-between">
            <span class="text-16px font-700">我发起的流程</span>
            <el-button link type="primary" @click="handleMoreProcess">查看更多</el-button>
          </div>
        </template>
        <el-skeleton :loading="loading" animated>
          <el-table
            :data="myProcessList"
            stripe
            style="width: 100%"
            :show-header="true"
            size="small"
          >
            <el-table-column label="流程名称" prop="name" min-width="160" show-overflow-tooltip />
            <el-table-column label="流程分类" prop="categoryName" width="100" align="center" />
            <el-table-column label="状态" width="120" align="center">
              <template #default="scope">
                <dict-tag :type="DICT_TYPE.BPM_PROCESS_INSTANCE_STATUS" :value="scope.row.status" />
              </template>
            </el-table-column>
            <el-table-column label="发起时间" width="160" align="center">
              <template #default="scope">
                {{ formatDate(scope.row.startTime) }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="80" align="center">
              <template #default="scope">
                <el-button link type="primary" @click="handleProcessDetail(scope.row)"
                  >详情</el-button
                >
              </template>
            </el-table-column>
          </el-table>
          <el-empty
            v-if="!loading && myProcessList.length === 0"
            description="暂无流程记录"
            :image-size="80"
          />
        </el-skeleton>
      </el-card>
    </el-col>

    <el-col :xl="8" :lg="8" :md="24" :sm="24" :xs="24" class="mb-8px">
      <el-card shadow="never">
        <template #header>
          <div class="flex items-center justify-between">
            <span class="text-16px font-700">站内信</span>
            <el-button link type="primary" @click="handleMoreMessage">更多</el-button>
          </div>
        </template>
        <el-skeleton :loading="loading" animated>
          <div v-for="(item, index) in notifyList" :key="`notify-${index}`">
            <div class="flex items-start py-4px">
              <el-badge
                is-dot
                :hidden="item.readStatus"
                class="mr-8px mt-8px"
                style="flex-shrink: 0"
              />
              <div class="flex-1 overflow-hidden">
                <div class="text-14px truncate"> {{ item.templateContent }} </div>
                <div class="mt-4px text-12px text-gray-400">
                  {{ item.templateNickname }} · {{ formatDate(item.createTime) }}
                </div>
              </div>
            </div>
            <el-divider v-if="index < notifyList.length - 1" class="!my-8px" />
          </div>
          <el-empty
            v-if="!loading && notifyList.length === 0"
            description="暂无站内信"
            :image-size="80"
          />
        </el-skeleton>
      </el-card>
    </el-col>
  </el-row>
</template>

<script lang="ts" setup>
import { formatDate } from '@/utils/formatTime'
import { DICT_TYPE } from '@/utils/dict'
import { useUserStore } from '@/store/modules/user'
import { getNoticePage } from '@/api/system/notice'
import {
  getUnreadNotifyMessageCount,
  getUnreadNotifyMessageList
} from '@/api/system/notify/message'
import { getTaskTodoPage } from '@/api/bpm/task'
import { getProcessInstanceMyPage, getProcessInstanceCopyPage } from '@/api/bpm/processInstance'
import { getUserProfile } from '@/api/system/user/profile'
import type { Notice } from './types'

defineOptions({ name: 'Index' })

const { push } = useRouter()
const userStore = useUserStore()
const loading = ref(true)
const avatar = userStore.getUser.avatar
const username = userStore.getUser.nickname

const deptName = ref('')
const roleName = ref('')

const greeting = computed(() => {
  const hour = new Date().getHours()
  if (hour < 6) return '凌晨好'
  if (hour < 9) return '早上好'
  if (hour < 12) return '上午好'
  if (hour < 14) return '中午好'
  if (hour < 17) return '下午好'
  if (hour < 19) return '傍晚好'
  if (hour < 22) return '晚上好'
  return '夜里好'
})

const statsData = reactive({
  unreadMessage: 0,
  todoTask: 0,
  myProcess: 0,
  copyProcess: 0
})

const activeNoticeTab = ref('notice')
let notice = reactive<Notice[]>([])
const todoList = ref<any[]>([])
const myProcessList = ref<any[]>([])
const notifyList = ref<any[]>([])

const getStats = async () => {
  try {
    const [unreadRes, todoRes, processRes, copyRes] = await Promise.all([
      getUnreadNotifyMessageCount(),
      getTaskTodoPage({ pageNo: 1, pageSize: 1 }),
      getProcessInstanceMyPage({ pageNo: 1, pageSize: 1 }),
      getProcessInstanceCopyPage({ pageNo: 1, pageSize: 1 })
    ])
    statsData.unreadMessage = unreadRes || 0
    statsData.todoTask = todoRes?.total || 0
    statsData.myProcess = processRes?.total || 0
    statsData.copyProcess = copyRes?.total || 0
  } catch (error) {
    console.error('获取统计数据失败:', error)
  }
}

const getNotice = async () => {
  try {
    const type = activeNoticeTab.value === 'notice' ? 1 : 2
    const res = await getNoticePage({ pageNo: 1, pageSize: 5, status: 0, type } as any)
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

const handleTabChange = () => {
  getNotice()
}

const getTodoList = async () => {
  try {
    const res = await getTaskTodoPage({ pageNo: 1, pageSize: 5 })
    todoList.value = res?.list || []
  } catch (error) {
    console.error('获取待办列表失败:', error)
  }
}

const getMyProcessList = async () => {
  try {
    const res = await getProcessInstanceMyPage({ pageNo: 1, pageSize: 5 })
    myProcessList.value = res?.list || []
  } catch (error) {
    console.error('获取我的流程失败:', error)
  }
}

const getNotifyList = async () => {
  try {
    const res = await getUnreadNotifyMessageList()
    notifyList.value = (res || []).slice(0, 5)
  } catch (error) {
    console.error('获取站内信失败:', error)
  }
}

const getUserProfileData = async () => {
  try {
    const res = await getUserProfile()
    if (res) {
      deptName.value = res.dept?.name || ''
      roleName.value = res.roles?.map((r: any) => r.name).join('、') || ''
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
  }
}

const handleCardClick = (type: string) => {
  switch (type) {
    case 'message':
      push({ name: 'MyNotifyMessage' })
      break
    case 'todo':
      push({ name: 'BpmTodoTask' })
      break
    case 'process':
      push({ name: 'BpmProcessInstanceMy' })
      break
    case 'copy':
      push({ name: 'BpmProcessInstanceCopy' })
      break
  }
}

const handleAudit = (row: any) => {
  push({
    name: 'BpmProcessInstanceDetail',
    query: { id: row.processInstance?.id, taskId: row.id }
  })
}

const handleProcessDetail = (row: any) => {
  push({
    name: 'BpmProcessInstanceDetail',
    query: { id: row.id }
  })
}

const handleMoreTodo = () => {
  push({ name: 'BpmTodoTask' })
}

const handleMoreProcess = () => {
  push({ name: 'BpmProcessInstanceMy' })
}

const handleMoreNotice = () => {
  push({ name: 'SystemNotice' })
}

const handleMoreMessage = () => {
  push({ name: 'MyNotifyMessage' })
}

const getAllApi = async () => {
  await Promise.all([
    getStats(),
    getNotice(),
    getTodoList(),
    getMyProcessList(),
    getNotifyList(),
    getUserProfileData()
  ])
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
