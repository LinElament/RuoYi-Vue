<template>
    <div ref="floatBall" class="floating-ball" :class="{ 'is-open': isOpen, 'is-dragging': isDragging }"
        @mousedown="startDrag" @touchstart="startDrag" @mouseup="endDrag" @touchend="endDrag" @mouseleave="endDrag">
        <transition name="fade">
            <div class="form-container" v-if="isOpen">
                <!-- 表单内容 -->
                <form @submit.prevent="submitForm">
                    <!-- 表单元素 -->
                </form>
            </div>
        </transition>
        <div class="ripple" v-if="!isOpen">
            <div class="ripple-wave" v-for="index in 3" :key="index"></div>
        </div>
    </div>
</template>
  
<script>
export default {
    data() {
        return {
            isOpen: false,
            isDragging: false,
            startX: 0,
            startY: 0,
            translateX: 0,
            translateY: 0,
            lastPosition: JSON.parse(localStorage.getItem('floatBallPosition')) || { x: 0, y: 0 }
        };
    },
    mounted() {
        this.restorePosition();
    },
    methods: {
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
            if (!this.isDragging) return;
            event.preventDefault();
            event = event.type.includes('touch') ? event.touches[0] : event;
            this.translateX = event.clientX - this.startX;
            this.translateY = event.clientY - this.startY;
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
        submitForm() {
            // 处理表单提交逻辑
        }
    }
};
</script>
  
<style scoped>
.floating-ball {
    position: fixed;
    bottom: 20px;
    right: 20px;
    width: 50px;
    height: 50px;
    border-radius: 50%;
    background-color: #333;
    cursor: pointer;
    transition: transform 0.3s ease;
    overflow: hidden;
}

.floating-ball.is-open .form-container {
    display: block;
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
  