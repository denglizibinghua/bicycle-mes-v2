<template>
  <div class="ai-chat-wrapper">
    <!-- 浮动按钮（面板关闭时显示） -->
    <div v-if="!visible" class="ai-fab" @click="open">
      <el-icon :size="24"><ChatDotRound /></el-icon>
    </div>

    <!-- 对话面板 -->
    <div
      v-if="visible"
      class="ai-panel"
      :class="{ 'ai-panel--dragging': dragging }"
      :style="panelStyle"
    >
      <!-- 标题栏（按住可拖拽） -->
      <div class="ai-header" @mousedown="startDrag">
        <span>🤖 MES 助手</span>
        <div class="ai-header-actions" @mousedown.stop>
          <!-- 缩放切换 -->
          <el-icon @click="toggleSize"><FullScreen /></el-icon>
          <!-- 最小化 -->
          <el-icon @click="minimize"><Minus /></el-icon>
          <!-- 关闭 -->
          <el-icon @click="close"><Close /></el-icon>
        </div>
      </div>

      <!-- 消息列表 -->
      <div class="ai-messages" ref="msgList">
        <div
          v-for="(msg, i) in messages"
          :key="i"
          :class="['ai-msg', msg.role === 'user' ? 'ai-msg--user' : 'ai-msg--ai']"
        >
          <div class="ai-msg-bubble">{{ msg.content }}</div>
          <!-- 导航按钮 -->
          <div
            v-if="msg.action === 'navigate' && msg.data"
            class="ai-nav-btn"
            @click="goPage(msg.data.route)"
          >
            📂 打开「{{ msg.data.label }}」
          </div>
        </div>
        <!-- 加载中 -->
        <div v-if="loading" class="ai-msg ai-msg--ai">
          <div class="ai-msg-bubble ai-thinking">AI 思考中...</div>
        </div>
      </div>

      <!-- 输入区 -->
      <div class="ai-input">
        <el-input
          v-model="inputText"
          placeholder="问我任何 MES 相关问题..."
          type="textarea"
          :rows="1"
          resize="none"
          @keyup.enter.exact="send"
          :disabled="loading"
        />
        <el-button
          type="primary"
          @click="send"
          :disabled="loading || !inputText.trim()"
          :icon="Promotion"
          circle
          size="small"
        />
      </div>
    </div>
  </div>
</template>

<script>
import { aiChat } from '@/api/mes/ai'
import { useRouter } from 'vue-router'
import { ChatDotRound, Close, Promotion, Minus, FullScreen } from '@element-plus/icons-vue'

export default {
  name: 'AiChat',
  components: {
    ChatDotRound,
    Close,
    Promotion,
    Minus,
    FullScreen
  },
  setup() {
    const router = useRouter()
    return { router }
  },
  data() {
    return {
      visible: false,
      loading: false,
      inputText: '',
      messages: [],
      // 拖拽与缩放相关状态
      dragging: false,        // 是否正在拖拽
      enlarged: false,         // 是否大尺寸（true=560×680，false=380×520）
      panelLeft: null,         // 拖拽后的 left，null 表示用默认右下定位
      panelTop: null
    }
  },
  computed: {
    // 面板样式：尺寸 + 定位
    panelStyle() {
      const style = {
        width: this.enlarged ? '560px' : '380px',
        height: this.enlarged ? '680px' : '520px'
      }
      if (this.panelLeft !== null && this.panelTop !== null) {
        // 已拖拽：用 left/top 定位
        style.left = this.panelLeft + 'px'
        style.top = this.panelTop + 'px'
        style.right = 'auto'
        style.bottom = 'auto'
      } else {
        // 默认：右下角定位
        style.right = '24px'
        style.bottom = '24px'
      }
      return style
    }
  },
  methods: {
    open() {
      this.visible = true
      // 首次打开时显示欢迎消息
      if (this.messages.length === 0) {
        this.messages.push({
          role: 'ai',
          content:
            '你好！我是 MES 助手，你可以：\n• 说"打开工单管理"来跳转页面\n• 询问工单统计、报工数据\n• 说"创建一个车架工单，焊接产线，100个"来创建工单'
        })
      }
      this.$nextTick(() => this.scrollToBottom())
    },

    close() {
      this.visible = false
    },

    // 最小化：收起面板回到浮动按钮（保留消息状态）
    minimize() {
      this.visible = false
    },

    // 切换面板大小（默认 ↔ 大尺寸）
    toggleSize() {
      this.enlarged = !this.enlarged
      // 尺寸变化后重新校正位置，避免越界
      this.clampPosition()
    },

    // 开始拖拽：记录起点并绑定全局 mousemove/mouseup
    startDrag(e) {
      const rect = this.$el.querySelector('.ai-panel').getBoundingClientRect()
      const startX = e.clientX
      const startY = e.clientY
      const startLeft = rect.left
      const startTop = rect.top

      // 首次拖拽：从默认右下定位切到 left/top 定位
      this.panelLeft = rect.left
      this.panelTop = rect.top
      this.dragging = true

      // 拖拽中：根据鼠标偏移更新位置并限制边界
      const onMove = (ev) => {
        const dx = ev.clientX - startX
        const dy = ev.clientY - startY
        this.panelLeft = startLeft + dx
        this.panelTop = startTop + dy
        this.clampPosition()
      }
      // 结束拖拽：解绑事件
      const onUp = () => {
        this.dragging = false
        document.removeEventListener('mousemove', onMove)
        document.removeEventListener('mouseup', onUp)
      }
      document.addEventListener('mousemove', onMove)
      document.addEventListener('mouseup', onUp)
    },

    // 边界限制：保证面板至少有 50px 可见在视口内
    clampPosition() {
      if (this.panelLeft === null) return
      const w = this.enlarged ? 560 : 380
      const h = this.enlarged ? 680 : 520
      const margin = 50
      // left 范围：[margin - w, innerWidth - margin]（右侧或左侧至少留 50px）
      const minLeft = margin - w
      const maxLeft = window.innerWidth - margin
      // top 范围：[margin - h, innerHeight - margin]
      const minTop = margin - h
      const maxTop = window.innerHeight - margin
      this.panelLeft = Math.max(minLeft, Math.min(maxLeft, this.panelLeft))
      this.panelTop = Math.max(minTop, Math.min(maxTop, this.panelTop))
    },

    async send() {
      const text = this.inputText.trim()
      if (!text || this.loading) return

      this.messages.push({ role: 'user', content: text })
      this.inputText = ''
      this.loading = true
      this.$nextTick(() => this.scrollToBottom())

      try {
        const res = await aiChat(text)
        const data = res.data || res // 适配不同的响应包装

        const msg = {
          role: 'ai',
          content: data.reply || '抱歉，我暂时无法回答这个问题',
          action: data.action || null,
          data: data.data || null
        }
        this.messages.push(msg)
      } catch (e) {
        this.messages.push({
          role: 'ai',
          content: '😵 服务暂时不可用，请稍后再试'
        })
      } finally {
        this.loading = false
        this.$nextTick(() => this.scrollToBottom())
      }
    },

    goPage(route) {
      if (route) {
        this.router.push(route)
        this.close()
      }
    },

    scrollToBottom() {
      const el = this.$refs.msgList
      if (el) el.scrollTop = el.scrollHeight
    }
  }
}
</script>

<style scoped lang="scss">
.ai-chat-wrapper {
  position: fixed;
  bottom: 24px;
  right: 24px;
  z-index: 9999;
}

.ai-fab {
  width: 56px;
  height: 56px;
  border-radius: 50%;
  background: linear-gradient(135deg, #1890ff, #096dd9);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 4px 12px rgba(24, 144, 255, 0.4);
  transition: transform 0.2s;

  &:hover {
    transform: scale(1.1);
  }
}

.ai-panel {
  position: fixed;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
  display: flex;
  flex-direction: column;
  overflow: hidden;
  // 缩放切换时尺寸有过渡动画
  transition: width 0.2s ease, height 0.2s ease;
}

// 拖拽过程中禁用过渡，保证跟手
.ai-panel--dragging {
  transition: none !important;
  user-select: none;
}

.ai-header {
  padding: 14px 18px;
  background: linear-gradient(135deg, #1890ff, #096dd9);
  color: #fff;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 15px;
  font-weight: 500;
  cursor: move; // 提示可拖拽

  .ai-header-actions {
    display: flex;
    align-items: center;
    gap: 12px;

    .el-icon {
      cursor: pointer;
      font-size: 18px;
      transition: opacity 0.2s;

      &:hover {
        opacity: 0.7;
      }
    }
  }
}

.ai-messages {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  background: #f5f7fa;

  .ai-msg {
    margin-bottom: 12px;
    display: flex;
    flex-direction: column;

    &--user {
      align-items: flex-end;
    }

    &--ai {
      align-items: flex-start;
    }
  }

  .ai-msg-bubble {
    max-width: 80%;
    padding: 10px 14px;
    border-radius: 12px;
    font-size: 14px;
    line-height: 1.6;
    white-space: pre-wrap;
    word-break: break-word;
  }

  .ai-msg--user .ai-msg-bubble {
    background: #1890ff;
    color: #fff;
    border-bottom-right-radius: 4px;
  }

  .ai-msg--ai .ai-msg-bubble {
    background: #fff;
    color: #333;
    border: 1px solid #e8e8e8;
    border-bottom-left-radius: 4px;
  }

  .ai-thinking {
    color: #999;
    font-style: italic;
    animation: pulse 1.5s infinite;
  }

  .ai-nav-btn {
    margin-top: 8px;
    padding: 8px 14px;
    background: #e6f7ff;
    border: 1px solid #91d5ff;
    border-radius: 6px;
    color: #1890ff;
    cursor: pointer;
    font-size: 13px;
    transition: background 0.2s;

    &:hover {
      background: #bae7ff;
    }
  }
}

.ai-input {
  padding: 12px;
  border-top: 1px solid #f0f0f0;
  display: flex;
  gap: 8px;
  align-items: center;
  background: #fff;

  :deep(.el-textarea__inner) {
    resize: none;
  }
}

@keyframes pulse {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0.5;
  }
}
</style>
