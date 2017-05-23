###Andorid实现垃圾桶开关动画

@(Android自定义View)[RubbshView]

[TOC]
####效果图如下
![enter image description here](http://chuantu.biz/t5/92/1495531504x2728309609.gif)
####实现思路
1. 先查分动画
	+ 垃圾桶盖部分
	+ 垃圾桶身体部分
2. 分别使用Paint,Path画出相应内容
3. 给垃圾桶盖添加动画
####具体实现
1. 测量布局
	测量View的宽高
```
    /**
     * 测量布局的宽高
     *
     * @param defaultSize 测量的默认值大小
     * @param measureSpec 测量参数
     * @return 测量后的width or height
     */
    private int getMeasureSize(int defaultSize, int measureSpec) {
        int resultSize = 0;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        switch (mode) {
            case MeasureSpec.AT_MOST:
                resultSize = defaultSize;
                break;
            case MeasureSpec.EXACTLY:
                resultSize = size;
                break;
            case MeasureSpec.UNSPECIFIED:
                resultSize = size;
                break;
        }
        return resultSize;
    }
```
测量好布局之后给View赋值
```

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasureSize(defaultSize, widthMeasureSpec);
        int height = getMeasureSize(defaultSize, heightMeasureSpec);
        setMeasuredDimension(width, height);
        //賦值view的寬和高
        mWidth = width > 0 ? width : 0;
        mHeight = height > 0 ? height : 0;
    }
```
2.  画出垃圾桶下面的区域和中间的三条竖线
	+ 第一将Path moveTo 第一个点(左上角点)
	+ 第二连接左下角顶点
	+ 第三连接右下角顶点
	+ 连接右上角顶点
	+ 接下来画中间三条竖线
```
    /**
     * 画出垃圾桶下面区域
     */
    private void drawBottom() {
        //花垃圾桶边缘
        //1.移动到最左上角的点
        //2.连接左下角的点
        //3.连接右下角的点
        //4.连接右上角的点
        mPath.moveTo(mWidth / 2 - (mBodyWith / 2), mHeight / 2 - (mBodyHeight / 2));
        mPath.lineTo(mWidth / 2 - (mBodyWith / 3), mHeight / 2 + (mBodyHeight / 2));
        mPath.lineTo(mWidth / 2 + (mBodyWith / 3), mHeight / 2 + (mBodyHeight / 2));
        mPath.lineTo(mWidth / 2 + (mBodyWith / 2), mHeight / 2 - (mBodyHeight / 2));
        //画里面的竖线
        //1.画左边一条线
        mPath.moveTo(mWidth / 2 - (mBodyWith / 5), mHeight / 2 - (mBodyHeight / 3));
        mPath.lineTo(mWidth / 2 - (mBodyWith / 5), mHeight / 2 + (mBodyHeight / 3));
        //2.画右边一条线
        mPath.moveTo(mWidth / 2 + (mBodyWith / 5), mHeight / 2 - (mBodyHeight / 3));
        mPath.lineTo(mWidth / 2 + (mBodyWith / 5), mHeight / 2 + (mBodyHeight / 3));
        //3.画中间一条线
        mPath.moveTo(mWidth / 2, mHeight / 2 - (mBodyHeight / 3));
        mPath.lineTo(mWidth / 2, mHeight / 2 + (mBodyHeight / 3));
    }
```
3. 画出静止垃圾桶盖
	+ 先画出线条
```
          //1画出禁止状态的垃圾桶盖子线条
            canvas.drawLine(mWidth / 2 - (mBodyWith / 2) - 10, mHeight / 2 - (mBodyHeight / 2) - 10,
                    mWidth / 2 + (mBodyWith / 2) + 10, mHeight / 2 - (mBodyHeight / 2) - 10, mPaint);
```
	+ 在画出矩形
```
//2画出静止状态盖子上面的把手
            canvas.drawRect(mWidth / 2 - (mBodyWith / 9), mHeight / 2 - (mBodyHeight / 2) - 20, mWidth / 2 + (mBodyWith / 9),
                    mHeight / 2 - (mBodyHeight / 2) - 10, mPaint);
```
4. 给垃圾桶盖添加动画
```
  //3.画垃圾桶打开动画
            canvas.rotate(openProgress * 30, mWidth / 2 + (mBodyWith / 2), mHeight / 2 - (mBodyHeight / 2));
```
给外部暴露一个调用动画的方法
```java
    /**
     * 开始动画
     */
    public void startAnimation() {
        animator = ValueAnimator.ofFloat(1f, 0f);
        animator.setDuration(2000);
        animator.start();
        invalidate();
    }
```
最后在onDraw方法中根据View的状态去画相应的状态就OK
```
        //动画判断是否刷新视图
        if (animator != null && animator.isRunning()) {
            //动画执行过程中具体帧值
            openProgress = (Float) animator.getAnimatedValue();
            drawTop(canvas, 0);
            drawTop(canvas, 1);
            drawTop(canvas, 2);
            invalidate();
        } else if (animator != null && !animator.isRunning()) {
            drawTop(canvas, 1);
            drawTop(canvas, 2);
        } else {//禁止状态
            drawTop(canvas, 1);
            drawTop(canvas, 2);
        }
```
   

