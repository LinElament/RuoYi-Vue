<template>
  <div id="content">
    <vue-waterfall-easy ref="waterfall" :imgsArr="imgsArr" @scrollReachBottom="getData" @click="clickFn"
      @imgError="imgErrorFn">
      <div class="img-info" slot-scope="props">
        <p class="some-info">第{{ props.index + 1 }}落地页</p>
        <p class="some-info">{{ props.value.info }}</p>
        <img :src="props.value.img" @load="imgLoaded" @error="imgErrorFn" class="img-wraper" />
      </div>
    </vue-waterfall-easy>
  </div>
</template>

<script>
import vueWaterfallEasy from 'vue-waterfall-easy'
import axios from 'axios'
import { getlist } from "@/api/landpage/data"

export default {
  name: 'app',
  data() {
    return {
      imgsArr: [],
      group: 0,
      isFetching: false,
      noMoreData: false
    }
  },
  components: {
    vueWaterfallEasy
  },
  methods: {
    async getData() {
      if (this.isFetching || this.noMoreData) return
      this.isFetching = true
      try {
        const res = await getlist({ group: this.group })
        if (res.data.length) {
          this.imgsArr = [...this.imgsArr, ...res.data]
          this.group++
        } else {
          this.noMoreData = true
          this.$refs.waterfall.waterfallOver()
        }
      } catch (error) {
        console.error('Error fetching data:', error)
      }
      this.isFetching = false
    },
    clickFn(event, { index, value }) {
      event.preventDefault()
      if (event.target.tagName.toLowerCase() == 'img') {
        console.log('img clicked', index, value)
      }
    },
    imgLoaded(event) {
      event.target.classList.add('loaded')
    },
    imgErrorFn(event) {
      // 检查事件目标是否存在，是图片元素，并且仍在DOM中
      if (event && event.target && event.target.nodeName === 'IMG' && document.body.contains(event.target)) {
        // 设置默认图片源
        event.target.src = 'https://api.btstu.cn/sjbz/api.php'; // 替换为你的默认图片路径
      } else {
        // 记录错误或处理事件目标不符合预期的情况
        console.error('Error: event target is not an image element, event is not defined, or element is not in DOM.');
      }
    },
  },
  created() {
    this.getData()
  }
}
</script>

<style scoped>
.img-wraper {
  transition: opacity 0.5s ease-in-out;
  opacity: 0;
}

.img-wraper.loaded {
  opacity: 1;
}

@media (max-width: 768px) {
  /* 添加你的响应式样式 */
}
</style>
