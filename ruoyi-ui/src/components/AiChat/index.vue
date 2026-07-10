<template>
  <div class="ai-chat-wrapper">
    <!-- 浮动按钮（面板关闭时显示） -->
    <div v-if="!visible" class="ai-fab" @click="open">
      <el-icon :size="24"><ChatDotRound /></el-icon>
    </div>

    <!-- 对话面板 -->
    <div v-if="visible" class="ai-panel">
      <!-- 标题栏 -->
      <div class="ai-header">
        <span>🤖 MES 助手</span>
        <el-icon @click="close"><Close /></el-icon>
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
import { ChatDotRound, Close, Promotion } from '@element-plus/icons-vue'

export default {
  name: 'AiChat',
  components: {
    ChatDotRound,
    Close,
    Promotion
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
      messages: []
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
            '你好！我是 MES 助手，你可以：\n• 说"打开工单管理"来跳转页面\n• 询问工单统计、报工数据\n• 让我帮你创建生产工单'
        })
      }
      this.$nextTick(() => this.scrollToBottom())
    },

    close() {
      this.visible = false
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
  width: 380px;
  height: 520px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
  display: flex;
  flex-direction: column;
  overflow: hidden;
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
  cursor: default; // 防止拖拽

  .el-icon {
    cursor: pointer;
    font-size: 18px;
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
