<template>
  <div class="waterfall" @mousedown="handleMouseDown" @mouseup="handleMouseUp" @mouseleave="handleMouseLeave"
    @touchstart="handleTouchStart" @touchend="handleTouchEnd">
    <div class="waterfall-item" v-for="(item, index) in dataList" :key="index">
      <a :href="item.href" target="_blank">
        <img :src="item.src" @error="handleImageError" :data-has-error="false"
          @contextmenu.prevent="handleRightClick(item.file, index)">
      </a>
      <div class="href-display" @click="copyToClipboard(item.href)">
        <strong>{{ item.href }}</strong>
      </div>
    </div>
  </div>
</template>

<script>
import { getlist, deleteConfig } from "@/api/landpage/data"

export default {
  data() {
    return {
      dataList: [],
      defaultImage: require("@/assets/401_images/401.gif"), // 设置默认图片路径
      startY: 0, // 用于存储开始的Y位置
      currentY: 0, // 用于存储当前的Y位置
      isPulling: false, // 是否正在下拉
      isRefreshing: false, // 是否达到刷新阈值
      threshold: 150, // 触发刷新的阈值
      isDragging: false, // 是否正在拖动
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    handleMouseDown(event) {
      this.startY = event.clientY;
      this.isDragging = true;
    },
    handleMouseUp(event) {
      if (this.isDragging && event.clientY - this.startY > this.threshold) {
        this.fetchData();
      }
      this.isDragging = false;
    },
    handleMouseLeave() {
      this.isDragging = false;
    },
    // 触摸事件处理方法
    handleTouchStart(event) {
      this.startY = event.touches[0].clientY;
    },
    handleTouchEnd(event) {
      const endY = event.changedTouches[0].clientY;
      if (this.startY < endY && endY - this.startY > this.threshold) {
        this.fetchData();
      }
    },
    fetchData() {
      getlist().then(response => {
        this.dataList = response.data.map(item => ({
          ...item
        }));
      }).catch(error => {
        console.error('获取数据失败:', error);
      });
    },
    handleImageError(event) {
      if (!event.target.dataset.hasError) {
        event.target.src = this.defaultImage; // 图片加载失败时设置为默认图片
        event.target.dataset.hasError = true; // 避免重复加载
      }
    },
    copyToClipboard(text) {
      navigator.clipboard.writeText(text).then(() => {
        this.$modal.msgSuccess('链接已复制到剪贴板');
      }).catch(err => {
        console.error('复制失败:', err);
      });
    },
    handleRightClick(file, item) {
      let path = Array.of(file.slice(0, 135));
      console.log(path)
      let index = item;
      let ls = this.dataList;
      console.log(index);
      this.$modal.confirm('确认信息删除此落地页？').then(function () {
        deleteConfig(path).then(() => {
          ls.splice(index, 1);
        })
      });
    }
  }
};
</script>




<style scoped>
.loader {
  /* 初始状态 */
  opacity: 0;
  transform: translateY(-100%);
  transition: opacity 0.3s, transform 0.3s;
}

.waterfall.is-refreshing .loader {
  /* 刷新状态 */
  opacity: 1;
  transform: translateY(0);
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }

  100% {
    transform: rotate(360deg);
  }
}

.loader::after {
  /* 加载动画 */
  content: '';
  display: block;
  width: 30px;
  height: 30px;
  border: 3px solid #f3f3f3;
  border-top: 3px solid #3498db;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

.waterfall {
  column-count: 4;
  column-gap: 50px;
}


.waterfall-item {
  margin-bottom: 40px;
  border-radius: 30px 30px 16px 16px;
  break-inside: avoid;
  border: 2px solid #000000;
}

.waterfall-item a {
  display: block;
  background-size: cover;
  background-position: center;

}

.waterfall-item img {
  border-radius: 28px;
  width: 100%;
  display: block;
  -webkit-user-drag: none;
  /* 针对Safari浏览器 */
  user-drag: none;
  /* 针对非Safari浏览器 */
  user-select: none;
  /* 防止用户选择图片 */
  -moz-user-select: none;
  /* 针对Firefox浏览器 */
  -ms-user-select: none;
  /* 针对IE和Edge浏览器 */
  user-select: none;
  /* 针对Chrome、Opera和Safari浏览器 */
}

.href-display {
  background-color: rgb(0, 0, 0);
  border-radius: 11px;
  color: #ffffff;
  /* href显示样式 */
  text-align: center;
  margin-top: 10px;
  /* 调整间距 */
  cursor: pointer;
  /* 显示可点击的鼠标样式 */
  cursor: pointer;
  word-break: break-all;
  /* 防止文本溢出 */
  overflow-wrap: break-word;
  /* 长单词或URLs断行 */
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  /* 半透明蒙版 */
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 10px;
}
</style>

