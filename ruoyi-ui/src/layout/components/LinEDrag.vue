<template>
    <div ref="floatBall" class="floating-ball" @click="toggleForm"
        :class="{ 'is-dragging': isDragging, 'is-loading': isLoading, 'is-expanded': isOpen }" @mousedown="startDrag"
        @touchstart="startDrag" @mouseup="endDrag" @touchend="endDrag" @mouseleave="endDrag">
        <transition name="fade" mode="out-in">
            <div class="form-container" v-if="isOpen" key="form" @click.stop>
                <form @submit.prevent="submitForm">
                    <input type="text" v-model="url" placeholder="Enter URL here" :disabled="isLoading" @click.stop
                        style="margin-bottom: 20px;" @input="checkURL()">
                    <button type="submit" :disabled="isLoading || !isValidURL">Submit</button>
                </form>
            </div>
            <div v-else key="content">
                <!-- 悬浮球内容 -->
            </div>
        </transition>

        <!-- 用户配置修改页 :rules="rules" -->
        <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
            <el-form-item label="落地页地址" prop="targetLink">
                <el-input v-model="form.targetLink" placeholder="请输入落地页地址"></el-input>
            </el-form-item>

            <el-form-item label="域名" prop="domain">
                <el-select v-model="form.targetCountry" multiple placeholder="请选域名">
                    <el-option label="韩国" value="KR"></el-option>
                    <el-option label="美国" value="US"></el-option>
                    <el-option label="马来西亚" value="MY"></el-option>
                    <el-option label="日本" value="JP"></el-option>
                    <el-option label="印度" value="IN"></el-option>
                    <div v-for="(link, index) in form.domain" :key="'domain' + index">
                        <el-input type="text" v-model="form.link[index]" placeholder="请输入跳转地址"></el-input>
                        <el-button @click="removeLink(index)">删除</el-button>
                    </div>
                </el-select>
            </el-form-item>

            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
            </div>

        </el-dialog>
    </div>
</template>
  
<script>
import { addpage } from "@/api/landpage/data"
export default {
    data() {
        return {
            isOpen: false,
            isDragging: false,
            isLoading: false,
            isValidURL: false,
            startX: 0,
            startY: 0,
            url: '',
            translateX: 0,
            translateY: 0,
            lastPosition: JSON.parse(localStorage.getItem('floatBallPosition')) || { x: 0, y: 0 }
        };
    },
    mounted() {
        this.updatePosition();
    },
    methods: {
        checkURL() {
            const pattern = new RegExp('^(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]$'); // fragment locator
            this.isValidURL = pattern.test(this.url);
        },
        toggleForm() {
            this.isOpen = !this.isOpen;
            this.updatePosition();
        },
        toggle() {
            this.isOpen = !this.isOpen;
            if (!this.isOpen) {
                this.savePosition();
            }
        },
        startDrag(event) {
            event = event.type.includes('touch') ? event.touches[0] : event;
            this.isDragging = true;
            this.startX = event.clientX - this.lastPosition.x;
            this.startY = event.clientY - this.lastPosition.y;
            document.addEventListener('mousemove', this.dragging);
            document.addEventListener('touchmove', this.dragging, { passive: false });
        },
        dragging(event) {
            event.preventDefault();
            if (!this.isDragging) return;
            // 获取鼠标位置或触摸位置
            let clientX = event.type.includes('touch') ? event.touches[0].clientX : event.clientX;
            let clientY = event.type.includes('touch') ? event.touches[0].clientY : event.clientY;
            // 计算新位置
            let newX = clientX - this.startX;
            let newY = clientY - this.startY;
            // 获取悬浮球尺寸和网页尺寸
            let ballRect = this.$refs.floatBall.getBoundingClientRect();
            let pageWidth = document.documentElement.clientWidth;
            let pageHeight = document.documentElement.clientHeight;
            // 确保悬浮球不会被拖动出网页可视区域
            newX = Math.max(0, Math.min(newX, pageWidth - ballRect.width));
            newY = Math.max(0, Math.min(newY, pageHeight - ballRect.height));
            // 更新悬浮球位置
            this.translateX = newX;
            this.translateY = newY;
            this.updatePosition();
        },
        endDrag() {
            this.isDragging = false;
            document.removeEventListener('mousemove', this.dragging);
            document.removeEventListener('touchmove', this.dragging);
            this.lastPosition.x = this.translateX;
            this.lastPosition.y = this.translateY;
            this.savePosition();
        },
        updatePosition() {
            // 确保悬浮球在视口内
            const ballWidth = this.$refs.floatBall.offsetWidth;
            const ballHeight = this.$refs.floatBall.offsetHeight;
            const screenWidth = window.innerWidth;
            const screenHeight = window.innerHeight;

            this.translateX = Math.min(screenWidth - ballWidth, this.translateX);
            this.translateY = Math.min(screenHeight - ballHeight, this.translateY);

            this.$refs.floatBall.style.transform = `translate(${this.translateX}px, ${this.translateY}px)`;
        },
        savePosition() {
            localStorage.setItem('floatBallPosition', JSON.stringify({ x: this.translateX, y: this.translateY }));
        },
        restorePosition() {
            const savedPosition = this.lastPosition;
            this.translateX = savedPosition.x;
            this.translateY = savedPosition.y;
            this.updatePosition();
        },
        async submitForm() {
            this.$modal.msg("正在下载落地页，请等待加载完成");
            this.isLoading = true;
            this.isOpen = !this.isOpen;
            console.log(this.url);
            await addpage(this.url)
                .then(response => {
                    if (response.code == 200) {
                        this.$modal.msgSuccess(response.msg);
                    } else {
                        this.$modal.msgError("错误反馈");
                    }
                    this.isLoading = false;
                });
        }
    }
}
</script>
  
<style scoped>
.floating-ball {
    /* 悬浮球样式 */
    position: fixed;
    bottom: 20px;
    right: 20px;
    z-index: 1000;
    transition: transform 0.3s ease;
    border-radius: 50%;
    background: conic-gradient(red, orange, yellow, green, blue, indigo, violet);
    /* 示例颜色 */
    width: 50px;
    /* 初始大小 */
    height: 50px;
    /* 初始大小 */
    display: flex;
    align-items: center;
    justify-content: center;
    text-align: center;
    cursor: pointer;
}

.form-container {
    /* 表单容器样式 */
    position: fixed;
    top: 50%;
    left: 50%;
    width: auto;
    transform: translate(-50%, -50%);
    z-index: 1001;
}

.floating-ball.is-expanded {
    transform: scale(4);
    /* 假设原始大小为50px，scale(4)将使其变为200px */
    width: 200px;
    /* 扩大后的大小 */
    height: 200px;
    /* 扩大后的大小 */
}

.floating-ball.is-open .form-container {
    display: block;
}

.is-loading {
    /* 加载动画样式 */
    animation: spin 1s linear infinite;
}

.floating-ball.is-dragging {
    opacity: 0.7;
    cursor: grabbing;
}

.ripple {
    position: relative;
    width: 100%;
    height: 100%;
    overflow: hidden;
}

.ripple-wave {
    position: absolute;
    top: 50%;
    left: 50%;
    width: 20px;
    height: 20px;
    background: rgba(255, 255, 255, 0.3);
    border-radius: 50%;
    transform: translate(-50%, -50%);
    animation: ripple-animation 1.5s infinite ease-out;
}

.ripple-wave::before,
.ripple-wave::after {
    content: '';
    position: absolute;
    top: 50%;
    left: 50%;
    width: 100%;
    height: 100%;
    border-radius: 50%;
    background: inherit;
    animation: inherit;
    transform: translate(-50%, -50%);
}

.ripple-wave::before {
    animation-delay: 0.5s;
}

.ripple-wave::after {
    animation-delay: 1s;
}

.loading-message {
    /* 提示信息样式 */
    margin-top: 10px;
}

@keyframes spin {
    0% {
        transform: rotate(0deg);
    }

    100% {
        transform: rotate(360deg);
    }
}

@keyframes ripple-animation {
    0% {
        transform: translate(-50%, -50%) scale(0);
        opacity: 1;
    }

    100% {
        transform: translate(-50%, -50%) scale(calc(1 + (var(--random-scale) / 100)));
        opacity: 0;
    }
}

.fade-enter-active,
.fade-leave-active {
    transition: opacity 0.5s;
}

.fade-enter,
.fade-leave-to {
    opacity: 0;
}
</style>
  