package com.arc.fast.transition.sample

import android.os.Parcelable
import com.chad.library.adapter.base.entity.MultiItemEntity
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
class TestItem(
    val id: String,
    val image: Int,
    val title: String,
    val content: String
) : MultiItemEntity, Parcelable {
    @IgnoredOnParcel
    override var itemType: Int = 0
}

object TestData {
    const val KEY_IMAGE = "KEY_IMAGE"
    const val KEY_TITLE = "KEY_TITLe"
    val simpleData by lazy {
        val data = arrayListOf<TestItem>()
        for (i in 1..5) {
            data.add(
                TestItem(
                    id = UUID.randomUUID().toString(),
                    image = R.mipmap.s1,
                    title = "一加 Buds Pro 2 耳机体验：全面拉满的多边形战士",
                    content = "TWS 真无线耳机这几年的飞速发展，生生地在耳机这个成熟的产业领域里开辟了一片蓝海，然后很快杀成了红海。如今的真无线耳机颇有智能手机市场光景，厂商们你争我夺，越来越卷。从外观到连接，从降噪到音质，接着又是空间音频，耳机上能想到的功能、体验，都快被厂商们榨干了。\n" +
                            "不过对于消费者来说就有福了，大家能够以更实惠的价格体验到功能更全面更舒适的 TWS 真无线耳机。而就在 1 月 4 日，又一款各方面都非常强悍的 TWS 真无线耳机产品发布了，那就是全新的一加 Buds Pro 2。"
                )
            )
            data.add(
                TestItem(
                    id = UUID.randomUUID().toString(),
                    image = R.mipmap.s2,
                    title = "iQOO Neo7 竞速版上手：双芯加持，帧率稳定不烫手",
                    content = "2022 年 10 月 20 日，iQOO Neo7 携带天玑 9000+ 提出双芯玩法。\n" +
                            "2022 年 12 月 8 日，我们迎来 iQOO Neo7 SE，全球首发天玑 8200，为性价比开拓新的思路与想法。\n" +
                            "而在 2022 年 12 月 29 日，iQOO Neo7 系列再迎新作 —iQOO Neo7 竞速版，在性能方面升级为骁龙 8+ 芯片，显示芯升级为独显 Pro+，与 iQOO Neo7 同样的双芯加持，其能否延续 iQOO Neo 7 的性能优势？IT之家现在就为大家带来 iQOO Neo7 竞速版上手评测。"
                )
            )
            data.add(
                TestItem(
                    id = UUID.randomUUID().toString(),
                    image = R.mipmap.s3,
                    title = "消息称鸿海已启动苹果 iPhone 15 高端新机试产导入，第一供应商稳了",
                    content = " 1 月 9 日消息，据台湾经济日报消息，按照惯例，苹果将于今年秋天推出 iPhone 15 / Pro 系列新机，鸿海旗下深圳观澜厂开始进入高端 iPhone 15 新机试产导入服务（NPI），为量产暖身，透露苹果仍高度依赖鸿海，为鸿海下半年业绩注入强大动能。\n" +
                            "值得注意的是，今年新 iPhone 的 NPI 与往年有一点不同，主要是中国大陆与印度两地 iPhone 量产时间差距将拉近，往年两地量产时间相隔约六到九个月，去年缩短至两个月，今年将再缩短至几周。"
                )
            )
            data.add(
                TestItem(
                    id = UUID.randomUUID().toString(),
                    image = R.mipmap.s4,
                    title = "消息称华为 P60 将采用 IMX789+IMX888 双大底主摄，搭配可变光圈",
                    content = "1 月 9 日消息，数码博主 @数码闲聊站 今日透露，华为 P60 系列将采用 IMX789 和 IMX888 两颗新大底主摄，其中 IMX888 有望为首发，两颗传感器均为 5000 万像素，拥有 1/1.4 的旗舰规格，而且新机还将采用可变光圈设计。\n" +
                            "消息称华为 P60 将采用 IMX789+IMX888 双大底主摄，搭配可变光圈\n" +
                            "值得一提的是，IMX789 传感器已经应用于一加 9 Pro，原生是 52Mp 1/1.35\" 规格，但一加为了成像设计裁为 48Mp，而华为这里也是裁切后的大小。"
                )
            )
        }
        return@lazy data
    }
}