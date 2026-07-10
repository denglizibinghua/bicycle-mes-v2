<template>
  <router-view />
  <AiChat />
</template>

<script setup>
import AiChat from '@/components/AiChat/index.vue'
import useSettingsStore from '@/store/modules/settings'
import { handleThemeStyle } from '@/utils/theme'

onMounted(() => {
  nextTick(() => {
    // 初始化主题样式
    handleThemeStyle(useSettingsStore().theme)
  })
})
</script>
