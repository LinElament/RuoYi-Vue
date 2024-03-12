<template>
  <div class="waterfall">
    <div class="waterfall-item" v-for="(item, index) in dataList" :key="index">
      <a :href="item.href" target="_blank">
        <img :src="item.src" />
      </a>
    </div>
  </div>
</template>

<script>
import { getlist } from "@/api/landpage/data"

export default {
  data() {
    return {
      dataList: []
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      getlist().then(response => {
        this.dataList = response.data;
        console.log(this.dataList);
      }).catch(error => {
        console.error('获取数据失败:', error);
      });
    }
  }
};
</script>

<style scoped>
.waterfall {
  column-count: 3;
  column-gap: 10px;
}

.waterfall-item {
  break-inside: avoid;
  margin-bottom: 10px;
}

.waterfall-item img {
  width: 100%;
  display: block;
  border-radius: 5px;
}
</style>
