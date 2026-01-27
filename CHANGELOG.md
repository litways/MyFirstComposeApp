# Changelog

## [0.3.0] - 2026-01-27
### 变更说明
- 总览、待办、发起、追溯、用户页面增加标题区域

### 本次 Codex 指令
````text
本次修改内容：

分别在以下页面中增加一个标题区域，用于显示当前页面名称：
- 总览页面
- 待办页面
- 发起页面
- 追溯页面
- 用户页面

页面标题区域 UI 设计规范（V1）如下（必须严格遵循）：

1）位置规范：
- 标题区域位于页面内容最顶部；
- 在页面根布局内直接声明；
- 不使用 TopAppBar，不使用 Scaffold 的 topBar；
- 标题区域随页面内容滚动，不悬浮、不覆盖内容。

2）尺寸规范：
- 标题区域高度随内容自适应；
- 不写死固定高度；
- 通过内边距形成区域感，不通过高度控制。

3）背景规范：
- 背景色使用蓝色；
- 优先使用 MaterialTheme.colorScheme.primary 或项目中已有主题色；
- 不新增颜色常量，不写死非主题色值。

4）文本规范：
- 仅显示当前页面名称文本；
- 不显示副标题、不显示图标；
- 使用 Material Design 3 默认文本样式；
- 不自定义 TextStyle，不新增字体或字号定义；
- 文本颜色优先使用主题自动适配（如 onPrimary）。

5）行为规范：
- 无任何交互行为；
- 不可点击、不响应手势；
- 不绑定业务状态。

6）工程实现规范：
- 不引入 ViewModel 状态；
- 不抽取公共组件；
- 每个页面独立实现，允许少量重复代码；
- 优先最小侵入、最小改动方案。

通用约束：
- 只允许修改各页面对应的文件；
- 不要修改其他页面；
- 不要改变现有 UI 布局和登录流程；
- 不要引入新的依赖；
- 不要影响其他页面的返回逻辑；
- 如需新增状态，请使用页面内的最小本地状态。

输出要求：
- 不要调整目录结构；
- 不要调整导航或页面结构；
- 不要重命名现有函数或变量；
- 不要移动文件；
- 只输出最终代码，不要包含解释说明；
- 保持 Material Design 3 的组件和样式不变；
- 修改后不能引入新的警告或编译错误；
- 如果不确定，请先选择最小改动方案；
- 修改完成后，请执行一次构建验证，确保项目可以正常编译；
- 不要口头声明“已测试”。如果无法执行测试，
  请明确说明原因，并至少保证项目可编译通过。

附加要求（必须执行）：
1）本次修改完成后，请更新 CHANGELOG.md：
   - 如果文件不存在，请新建；
   - 在文件顶部新增一个版本记录。
2）版本号规则：
   - 使用格式：0.x.y；
   - 本次属于新增功能，请递增 x，y 归零。
3）每个版本记录需包含以下内容：
   - 版本号
   - 修改日期（YYYY-MM-DD）
   - 本次修改说明（简要）
   - 本次使用的完整提示语/指令原文（原样写入）
4）CHANGELOG.md 只允许追加内容，不要修改历史版本记录。
5）除 CHANGELOG.md 和本次明确允许修改的页面文件外，
   不要修改其他文件。
6）只输出最终修改后的完整文件内容（包括 CHANGELOG.md）。
7）示例如下：
# Changelog
## [0.1.1] - 2026-01-27
### 本次 Codex 指令
XXXXXX
...
````

## [0.2.0] - 2026-01-27
### 变更说明
- 用户页面增加账号登出二次确认对话框

### 本次 Codex 指令
````text
# AGENTS.md instructions for /workspace/MyFirstComposeApp

<INSTRUCTIONS>
## Skills
A skill is a set of local instructions to follow that is stored in a `SKILL.md` file. Below is the list of skills that can be used. Each entry includes a name, description, and file path so you can open the source for full instructions when using a specific skill.
### Available skills
- skill-creator: Guide for creating effective skills. This skill should be used when users want to create a new skill (or update an existing skill) that extends Codex's capabilities with specialized knowledge, workflows, or tool integrations. (file: /opt/codex/skills/.system/skill-creator/SKILL.md)
- skill-installer: Install Codex skills into $CODEX_HOME/skills from a curated list or a GitHub repo path. Use when a user asks to list installable skills, install a curated skill, or install a skill from another repo (including private repos). (file: /opt/codex/skills/.system/skill-installer/SKILL.md)
### How to use skills
- Discovery: The list above is the skills available in this session (name + description + file path). Skill bodies live on disk at the listed paths.
- Trigger rules: If the user names a skill (with `$SkillName` or plain text) OR the task clearly matches a skill's description shown above, you must use that skill for that turn. Multiple mentions mean use them all. Do not carry skills across turns unless re-mentioned.
- Missing/blocked: If a named skill isn't in the list or the path can't be read, say so briefly and continue with the best fallback.
- How to use a skill (progressive disclosure):
  1) After deciding to use a skill, open its `SKILL.md`. Read only enough to follow the workflow.
  2) If `SKILL.md` points to extra folders such as `references/`, load only the specific files needed for the request; don't bulk-load everything.
  3) If `scripts/` exist, prefer running or patching them instead of retyping large code blocks.
  4) If `assets/` or templates exist, reuse them instead of recreating from scratch.
- Coordination and sequencing:
  - If multiple skills apply, choose the minimal set that covers the request and state the order you'll use them.
  - Announce which skill(s) you're using and why (one short line). If you skip an obvious skill, say why.
- Context hygiene:
  - Keep context small: summarize long sections instead of pasting them; only load extra files when needed.
  - Avoid deep reference-chasing: prefer opening only files directly linked from `SKILL.md` unless you're blocked.
  - When variants exist (frameworks, providers, domains), pick only the relevant reference file(s) and note that choice.
- Safety and fallback: If a skill can't be applied cleanly (missing files, unclear instructions), state the issue, pick the next-best approach, and continue.
</INSTRUCTIONS>

<environment_context>
  <cwd>/workspace/MyFirstComposeApp</cwd>
  <shell>bash</shell>
</environment_context>

在用户页面中，当用户点击“账号登出”按钮时，弹出一个二次确认对话框（使用 Material 3 的 AlertDialog）。

对话框文案
标题：提示  
内容：确定要退出当前用户登录吗？  
按钮：确认 / 取消  

行为要求：
-点击确认才执行现有退出逻辑；
-点击取消仅关闭对话框，不做任何操作。

约束：
-只允许修改用户页面对应的文件；
-不要修改其他页面；
-不要改变现有 UI 布局和登录流程；
-不要引入新的依赖；
-不要影响其他页面的返回逻辑；
-如需新增状态，请使用页面内的最小本地状态。

输出要求
-不要调整目录结构；
-不要调整导航或页面结构；
-不要重命名现有函数或变量；
-不要移动文件；
-只输出最终代码，不要包含解释说明；
-保持 Material Design 3 的组件和样式不变；
-修改后不能引入新的警告或编译错误；
-如果不确定，请先选择最小改动方案。
-修改完成后，请执行一次构建验证：确保项目可以正常编译（不允许新增编译错误或警告）。如果发现编译失败，请修复后再输出最终代码。
-不要口头声明“已测试”。如果无法执行测试，请明确说明无法执行的原因，并至少保证项目可编译通过。

附加要求（必须执行）：
1）本次修改完成后，请更新 CHANGELOG.md：
- 如果文件不存在，请新建。
- 在文件顶部新增一个版本记录。
2）版本号规则：
- 使用格式：0.x.y
- 如果是新增功能，递增 x，y 归零；
- 如果是修复或小改动，仅递增 y。
3）每个版本记录需包含以下内容：
- 版本号
- 修改日期（YYYY-MM-DD）
- 本次修改说明（简要）
- 本次使用的完整提示语/指令原文（原样写入）
4）CHANGELOG.md 只允许追加内容，不要修改历史版本记录。
5）除 CHANGELOG.md 和本次明确允许修改的代码文件外，
不要修改其他文件。
6）只输出最终修改后的完整文件内容（包括 CHANGELOG.md）。
7）示例如下：
# Changelog

## [0.1.1] - 2026-01-27
### 变更说明
- 登录页面增加退出二次确认对话框

### 本次 Codex 指令
```text
在 LoginScreen.kt 中，
当用户点击“退出”按钮时，弹出一个二次确认对话框。
...
```
````

## [0.1.0] - 2026-01-27
### 变更说明
- 登录页面增加退出二次确认对话框

### 本次 Codex 指令
````text
在登录页面中，当用户点击“退出”按钮时，弹出一个二次确认对话框（使用 Material 3 的 AlertDialog）。

对话框文案
标题：提示  
内容：确定要退出应用吗？  
按钮：确认 / 取消  

行为要求：
-点击确认才执行现有退出逻辑；
-点击取消仅关闭对话框，不做任何操作。

约束：
-只允许修改登录页面对应的文件；
-不要修改其他页面；
-不要改变现有 UI 布局和登录流程；
-不要引入新的依赖；
-不要影响其他页面的返回逻辑；
-如需新增状态，请使用页面内的最小本地状态。

输出要求
-不要调整目录结构；
-不要调整导航或页面结构；
-不要重命名现有函数或变量；
-不要移动文件；
-只输出最终代码，不要包含解释说明；
-保持 Material Design 3 的组件和样式不变；
-修改后不能引入新的警告或编译错误；
-如果不确定，请先选择最小改动方案。


附加要求（必须执行）：
1）本次修改完成后，请更新 CHANGELOG.md：
- 如果文件不存在，请新建。
- 在文件顶部新增一个版本记录。
2）版本号规则：
- 使用格式：0.x.y
- 如果是新增功能，递增 x，y 归零；
- 如果是修复或小改动，仅递增 y。
3）每个版本记录需包含以下内容：
- 版本号
- 修改日期（YYYY-MM-DD）
- 本次修改说明（简要）
- 本次使用的完整提示语/指令原文（原样写入）
4）CHANGELOG.md 只允许追加内容，不要修改历史版本记录。
5）除 CHANGELOG.md 和本次明确允许修改的代码文件外，
不要修改其他文件。
6）只输出最终修改后的完整文件内容（包括 CHANGELOG.md）。
7）示例如下：
# Changelog

## [0.1.1] - 2026-01-27
### 变更说明
- 登录页面增加退出二次确认对话框

### 本次 Codex 指令
```text
在 LoginScreen.kt 中，
当用户点击“退出”按钮时，弹出一个二次确认对话框。
...
```
````
