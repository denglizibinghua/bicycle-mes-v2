Component({
  data: {
    selected: 0,
    list: [],
  },

  lifetimes: {
    attached() {
      this.buildTabList()
    },
  },

  pageLifetimes: {
    show() {
      this.buildTabList()
      this.updateSelected()
    },
  },

  methods: {
    buildTabList() {
      const perms = this.getPermissions()
      const allTabs = [
        { text: '工作台', icon: '🏠', path: '/pages/dashboard/dashboard', perm: null },
        { text: '报工', icon: '🔧', path: '/pages/report/report', perm: 'mes:productionreport:add' },
        { text: '质检', icon: '🔍', path: '/pages/quality/quality', perm: 'mes:quality:add' },
        { text: '我的', icon: '👤', path: '/pages/mine/mine', perm: null },
      ]

      const visible = allTabs.filter((tab) => {
        if (tab.perm === null) return true
        return perms.includes('*:*:*') || perms.includes(tab.perm)
      })

      this.setData({ list: visible })
    },

    getPermissions() {
      try {
        const raw = wx.getStorageSync('permissions')
        return raw ? JSON.parse(raw) : []
      } catch {
        return []
      }
    },

    updateSelected() {
      const pages = getCurrentPages()
      const page = pages[pages.length - 1]
      if (page) {
        const route = '/' + page.route
        const idx = this.data.list.findIndex((t) => t.path === route)
        if (idx >= 0) {
          this.setData({ selected: idx })
        }
      }
    },

    switchTab(e) {
      const idx = e.currentTarget.dataset.index
      const tab = this.data.list[idx]
      wx.switchTab({ url: tab.path })
    },
  },
})
