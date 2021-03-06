package com.yuruiyin.richeditor.sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yuruiyin.richeditor.enumtype.FileTypeEnum
import com.yuruiyin.richeditor.enumtype.RichTypeEnum
import com.yuruiyin.richeditor.model.BlockImageSpanVm
import com.yuruiyin.richeditor.model.IBlockImageSpanObtainObject
import com.yuruiyin.richeditor.model.RichEditorBlock
import com.yuruiyin.richeditor.model.StyleBtnVm
import com.yuruiyin.richeditor.sample.enumtype.BlockImageSpanType
import com.yuruiyin.richeditor.sample.model.*
import com.yuruiyin.richeditor.sample.utils.DeviceUtil
import com.yuruiyin.richeditor.sample.utils.JsonUtil
import com.yuruiyin.richeditor.sample.utils.WindowUtil
import com.yuruiyin.richeditor.utils.BitmapUtil
import com.yuruiyin.richeditor.utils.FileUtil
import com.yuruiyin.richeditor.utils.ViewUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val GET_PHOTO_REQUEST_CODE = 1
        const val TAG = "MainActivity"

        // 测试用
        const val HUAWEI_IMAGE_PATH = "/storage/emulated/0/Huawei/MagazineUnlock/magazine-unlock-04-2.3.4312-_6DE13B88E5CE3D69DE5469945117A2A6.jpg"
        const val MUMU_IMAGE_PATH = "/storage/emulated/0/DCIM/challenge/122489.jpg"
        const val OPPO_IMAGE_PATH = "/storage/emulated/0/Pictures/magazine-unlock-04-2.3.4312-_6DE13B88E5CE3D69DE5469945117A2A6.jpg";

        /**
         * 草稿SharePreferences的名字
         */
        const val SP_DRAFT_NAME = "rich_editor"

        /**
         * 保存在SharePreferences中的草稿json数据key
         */
        const val KEY_DRAFT_JSON = "key_draft_json"
    }

    private val editorPaddingLeft by lazy {
        resources.getDimension(R.dimen.editor_padding_left)
    }

    private val editorPaddingRight by lazy {
        resources.getDimension(R.dimen.editor_padding_right)
    }

    private val imageWidth by lazy {
        resources.getDimension(R.dimen.editor_image_width).toInt()
    }

    private val imageMaxHeight by lazy {
        resources.getDimension(R.dimen.editor_image_max_height).toInt()
    }

    private val screenWidth by lazy {
        WindowUtil.getScreenSize(this)[0]
    }

    private val gameItemHeight by lazy {
        resources.getDimension(R.dimen.editor_game_height).toInt()
    }

    private val gameIconSize by lazy {
        resources.getDimension(R.dimen.editor_game_icon_size).toInt()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerEvents()
    }


    /**
     * 粗体
     */
    private fun initBold() {
        val styleBtnVm = StyleBtnVm.Builder()
                .setType(RichTypeEnum.BOLD)
                .setIvIcon(ivBold)
                .setIconNormalResId(R.mipmap.icon_bold_normal)
                .setIconLightResId(R.mipmap.icon_bold_light)
                .setClickedView(ivBold)
                .build()

        richEditText.initStyleButton(styleBtnVm)
    }

    /**
     * 斜体
     */
    private fun initItalic() {
        val styleBtnVm = StyleBtnVm.Builder()
                .setType(RichTypeEnum.ITALIC)
                .setIvIcon(ivItalic)
                .setIconNormalResId(R.mipmap.icon_italic_normal)
                .setIconLightResId(R.mipmap.icon_italic_light)
                .setClickedView(ivItalic)
                .build()

        richEditText.initStyleButton(styleBtnVm)
    }

    /**
     * 删除线
     */
    private fun initStrikeThrough() {
        val styleBtnVm = StyleBtnVm.Builder()
                .setType(RichTypeEnum.STRIKE_THROUGH)
                .setIvIcon(ivStrikeThrough)
                .setIconNormalResId(R.mipmap.icon_strikethrough_normal)
                .setIconLightResId(R.mipmap.icon_strikethrough_light)
                .setClickedView(ivStrikeThrough)
                .build()

        richEditText.initStyleButton(styleBtnVm)
    }

    /**
     * 下划线
     */
    private fun initUnderline() {
        val styleBtnVm = StyleBtnVm.Builder()
                .setType(RichTypeEnum.UNDERLINE)
                .setIvIcon(ivUnderline)
                .setIconNormalResId(R.mipmap.icon_underline_normal)
                .setIconLightResId(R.mipmap.icon_underline_light)
                .setClickedView(ivUnderline)
                .build()

        richEditText.initStyleButton(styleBtnVm)
    }

    /**
     * 标题
     */
    private fun initHeadline() {
        val styleBtnVm = StyleBtnVm.Builder()
                .setType(RichTypeEnum.BLOCK_HEADLINE)  // 指定为段落标题类型
                .setIvIcon(ivHeadline)       // 图标ImageView，用于修改高亮状态
                .setIconNormalResId(R.mipmap.icon_headline_normal)  // 正常图标
                .setIconLightResId(R.mipmap.icon_headline_light)    // 高亮图标
                .setClickedView(vgHeadline)  // 指定被点击的view
                .setTvTitle(tvHeadline)      // 按钮标题文字
                .setTitleNormalColor(ContextCompat.getColor(this@MainActivity, R.color.headline_normal_text_color)) // 正常标题文字颜色
                .setTitleLightColor(ContextCompat.getColor(this@MainActivity, R.color.headline_light_text_color))   // 高亮标题文字颜色
                .build()

        richEditText.initStyleButton(styleBtnVm)
    }

    /**
     * 下划线
     */
    private fun initBlockQuote() {
        val styleBtnVm = StyleBtnVm.Builder()
                .setType(RichTypeEnum.BLOCK_QUOTE)
                .setIvIcon(ivBlockquote)
                .setIconNormalResId(R.mipmap.icon_blockquote_normal)
                .setIconLightResId(R.mipmap.icon_blockquote_light)
                .setClickedView(ivBlockquote)
                .build()

        richEditText.initStyleButton(styleBtnVm)
    }

    public fun registerEvents() {
        // 生成json数据，显示到TextView上
        btnCreateJson.setOnClickListener {
            val draftEditorBlockList = convertEditorContent(richEditText.content)
            showJson(draftEditorBlockList)
        }

        // 清空内容
        btnClearContent.setOnClickListener {
            richEditText.clearContent()
        }

        // 保存草稿
        btnSaveDraft.setOnClickListener {
            handleSaveDraft()
        }

        // 恢复草稿
        btnRestoreDraft.setOnClickListener {
            handleRestoreDraft()
        }

        // 清空草稿
        btnClearDraft.setOnClickListener {
            handleClearDraft()
        }

        // 粗体
        initBold()

        // 斜体
        initItalic()

        // 删除线
        initStrikeThrough()

        // 下划线
        initUnderline()

        // 标题
        initHeadline()

        // 引用
        initBlockQuote()

        // 添加图片
        ivAddImage.setOnClickListener {
            handleAddImage()
        }

        // 添加分割线
        ivAddDivider.setOnClickListener {
            handleAddDivider()
        }

        // 添加游戏（自定义布局的一种）
        ivAddGame.setOnClickListener {
            handleAddGame()
        }

        // 添加同一张图片，用于测试插入多张图片导致的卡顿和OOM问题
        ivAddSameImageForTest.setOnClickListener {
            handleAddSameImageForTest()
        }

        ivUndo.setOnClickListener {
            if (!richEditText.isUndoRedoEnable) {
                Toast.makeText(this, "未开启undo redo功能", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            richEditText.undo()
        }

        ivRedo.setOnClickListener {
            if (!richEditText.isUndoRedoEnable) {
                Toast.makeText(this, "未开启undo redo功能", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            richEditText.redo()
        }

        // 组件内部默认不开启undo redo功能（由于undo redo功能会占用更大的内存）
//        richEditText.isUndoRedoEnable = true
    }

    private fun convertEditorContent(editorBlockList: List<RichEditorBlock>): List<DraftEditorBlock> {
        // 先将对象进行转换，让里头blockImageSpanObtainObject具体到各自类型的实体上（如ImageVm）
        val draftEditorBlockList = mutableListOf<DraftEditorBlock>()
        editorBlockList.forEach {
            val draftEditorBlock = DraftEditorBlock()
            draftEditorBlock.blockType = it.blockType
            draftEditorBlock.text = it.text
            draftEditorBlock.inlineStyleEntities = it.inlineStyleEntityList
            when (it.blockType) {
                BlockImageSpanType.IMAGE -> {
                    draftEditorBlock.image = it.blockImageSpanObtainObject as? ImageVm
                }
                BlockImageSpanType.VIDEO -> {
                    draftEditorBlock.video = it.blockImageSpanObtainObject as? VideoVm
                }
                BlockImageSpanType.GAME -> {
                    draftEditorBlock.game = it.blockImageSpanObtainObject as? GameVm
                }
                BlockImageSpanType.DIVIDER -> {
                    draftEditorBlock.divider = it.blockImageSpanObtainObject as? DividerVm
                }
            }
            draftEditorBlockList.add(draftEditorBlock)
        }

        return draftEditorBlockList
    }

    private fun showJson(draftEditorBlockList: List<DraftEditorBlock>) {
        val content = Gson().toJson(draftEditorBlockList)
        val formatJsonContent = JsonUtil.getFormatJson(content)
        tvContentJson.text = formatJsonContent
        Log.d(TAG, "\n $formatJsonContent")
    }

    private fun handleClearDraft() {
        val sp = getSharedPreferences(SP_DRAFT_NAME, Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.clear()
        editor.apply()

        Toast.makeText(this, "清空草稿成功", Toast.LENGTH_SHORT).show()
    }

    /**
     * 遍历段落恢复草稿，即一段一段的插入到编辑器中
     */
    private fun restoreDraft(draftEditorBlockList: List<DraftEditorBlock>) {
        richEditText.clearContent()
        draftEditorBlockList.forEach {
            when (it.blockType) {
                RichTypeEnum.BLOCK_NORMAL_TEXT, RichTypeEnum.BLOCK_HEADLINE, RichTypeEnum.BLOCK_QUOTE -> {
                    val richEditorBlock = RichEditorBlock()
                    richEditorBlock.blockType = it.blockType
                    richEditorBlock.text = it.text
                    richEditorBlock.inlineStyleEntityList = it.inlineStyleEntities
                    richEditText.insertBlockText(richEditorBlock)
                }
                // 以下就是用户自定义的blockType，可能是图片、视频、自定义类型等
                BlockImageSpanType.IMAGE -> {
                    val imageVm = it.image ?: return@forEach
                    doAddBlockImageSpan(imageVm.path, imageVm, true)
                }
                BlockImageSpanType.VIDEO -> {
                    val videoVm = it.video ?: return@forEach
                    doAddBlockImageSpan(videoVm.path, videoVm, true)
                }
                BlockImageSpanType.DIVIDER -> {
                    handleAddDivider(true)
                }
                BlockImageSpanType.GAME -> {
                    val gameVm = it.game ?: return@forEach
                    doAddGame(gameVm, true)
                }
            }
        }
    }

    /**
     * 恢复草稿
     */
    private fun handleRestoreDraft() {
        val sp = getSharedPreferences(SP_DRAFT_NAME, Context.MODE_PRIVATE)
        val jsonContent = sp.getString(KEY_DRAFT_JSON, "")
        if (TextUtils.isEmpty(jsonContent)) {
            Toast.makeText(this, "没有草稿内容", Toast.LENGTH_SHORT).show()
            return
        }

        val editorBlockList = Gson().fromJson<List<DraftEditorBlock>>(
                jsonContent,
                object : TypeToken<List<DraftEditorBlock>>() {}.type
        )

        showJson(editorBlockList)
        restoreDraft(editorBlockList)
    }

    /**
     * 保存草稿
     */
    private fun handleSaveDraft() {
        val richEditorBlockList = richEditText.content
        // 先将对象进行转换，让里头blockImageSpanObtainObject具体到各自类型的实体上（如ImageVm）
        val draftEditorBlockList = convertEditorContent(richEditorBlockList)

        val jsonContent = Gson().toJson(draftEditorBlockList)
        val sp = getSharedPreferences(SP_DRAFT_NAME, Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString(KEY_DRAFT_JSON, jsonContent)
//        editor.commit() // commit是同步写，可能会阻塞主线程，因此不建议
        editor.apply()

        Toast.makeText(this, "保存草稿成功", Toast.LENGTH_SHORT).show()
    }

    private fun getEditTextWidthWithoutPadding(): Int {
        // 富文本编辑器编辑区域的宽度, 这个宽度一定要小于编辑器的宽度，否则会出现ImageSpan被绘制两边的情况
        return (screenWidth - editorPaddingLeft - editorPaddingRight - 6).toInt()
    }

    private fun doAddGame(gameVm: GameVm, isFromDraft: Boolean = false) {
        val gameItemView = layoutInflater.inflate(R.layout.editor_game_item, null)
        val ivGameIcon = gameItemView.findViewById<ImageView>(R.id.ivGameIcon)
        val tvGameName = gameItemView.findViewById<TextView>(R.id.tvGameName)
        ivGameIcon.setImageResource(R.mipmap.icon_game_zhuoyao)
        tvGameName.text = gameVm.name

        ivGameIcon.layoutParams.width = gameIconSize
        ivGameIcon.layoutParams.height = gameIconSize

        val gameItemWidth = getEditTextWidthWithoutPadding()
        ViewUtil.layoutView(gameItemView, gameItemWidth, gameItemHeight)

        val blockImageSpanVm = BlockImageSpanVm(gameVm, gameItemWidth, imageMaxHeight)
        blockImageSpanVm.isFromDraft = isFromDraft
        richEditText.insertBlockImage(BitmapUtil.getBitmap(gameItemView), blockImageSpanVm) { blockImageSpan ->
            val retGameVm = blockImageSpan.blockImageSpanVm.spanObject as GameVm
            // 点击游戏item
            Toast.makeText(this, "短按了游戏：${retGameVm.name}", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 插入游戏
     */
    private fun handleAddGame() {
        val gameVm = GameVm(1, "一起来捉妖")
        doAddGame(gameVm)
        Log.d(TAG, "EditText的高度： " + richEditText.height)
    }

    /**
     * 添加同一张图片，用于测试插入多张图片导致的卡顿和OOM问题
     */
    fun handleAddSameImageForTest() {
        val realImagePath = if (DeviceUtil.isEmulator(this)) {
            MUMU_IMAGE_PATH
        } else {
            if (android.os.Build.MANUFACTURER == "HUAWEI") {
                HUAWEI_IMAGE_PATH
            } else {
                OPPO_IMAGE_PATH
            }
        }
        val imageVm = ImageVm(realImagePath, "2")
        doAddBlockImageSpan(realImagePath, imageVm)
        Log.d(TAG, "EditText的高度： " + richEditText.height)
    }

    /**
     * 处理添加分割线，其实插入的也是BlockImageSpan
     */
    private fun handleAddDivider(isFromDraft: Boolean = false) {
        val blockImageSpanVm =
                BlockImageSpanVm(DividerVm(), getEditTextWidthWithoutPadding(), imageMaxHeight)
        blockImageSpanVm.isFromDraft = isFromDraft
        richEditText.insertBlockImage(R.mipmap.image_divider_line, blockImageSpanVm, null)
    }

    /**
     * 处理插入图片
     */
    fun handleAddImage() {
        val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GET_PHOTO_REQUEST_CODE)
    }

    private fun doAddBlockImageSpan(
            realImagePath: String, blockImageSpanObtainObject: IBlockImageSpanObtainObject, isFromDraft: Boolean = false
    ) {
//        val blockImageSpanVm = BlockImageSpanVm(blockImageSpanObtainObject) // 不指定宽高，使用图片原始大小（但组件内对最大宽和最大高还是有约束的）
        val blockImageSpanVm = BlockImageSpanVm(blockImageSpanObtainObject, imageWidth, imageMaxHeight) // 指定宽高
        blockImageSpanVm.isFromDraft = isFromDraft
        richEditText.insertBlockImage(realImagePath, blockImageSpanVm) { blockImageSpan ->
            val spanObtainObject = blockImageSpan.blockImageSpanVm.spanObject
            when (spanObtainObject) {
                is ImageVm -> {
                    Toast.makeText(this, "短按了图片-当前图片路径：${spanObtainObject.path}", Toast.LENGTH_SHORT).show()
                }
                is VideoVm -> {
                    Toast.makeText(this, "短按了视频-当前视频路径：${spanObtainObject.path}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GET_PHOTO_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // 相册图片返回
            val selectedImageUri = data.data ?: return
            val realImagePath = FileUtil.getFileRealPath(this, selectedImageUri) ?: return
            Log.d(TAG, "realImagePath: $realImagePath")
            val fileType = FileUtil.getFileType(realImagePath) ?: return
            when (fileType) {
                FileTypeEnum.STATIC_IMAGE, FileTypeEnum.GIF -> {
                    val imageVm = ImageVm(realImagePath, "2")
                    doAddBlockImageSpan(realImagePath, imageVm)
                }
                FileTypeEnum.VIDEO -> {
                    // 插入视频封面
                    val videoVm = VideoVm(realImagePath, "3")
                    doAddBlockImageSpan(realImagePath, videoVm)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
        handleSaveDraft()
    }

}
