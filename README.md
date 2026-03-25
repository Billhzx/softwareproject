# D{0-1}背包问题求解器

## 项目简介

本项目是一个基于动态规划算法的D{0-1}背包问题求解器，提供了友好的图形用户界面，支持数据可视化、排序和结果导出功能。

## 功能特性

1. **数据文件读取**：支持读取标准格式的D{0-1}KP数据文件
2. **数据可视化**：绘制重量-价值散点图，直观展示数据分布
3. **数据排序**：按项集第三项的价值/重量比进行非递增排序
4. **最优解求解**：使用动态规划算法求解D{0-1}背包问题的最优解
5. **结果导出**：支持将求解结果导出为TXT或CSV格式
6. **友好界面**：提供直观的图形用户界面

## 项目结构

```
softwareproject/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── knapsack/
│   │               ├── model/          # 数据模型
│   │               ├── algorithm/      # 算法实现
│   │               ├── io/             # 输入输出
│   │               ├── ui/             # 用户界面
│   │               └── util/           # 工具类
│   └── resources/                     # 资源文件
├── docs/                              # 文档
├── lib/                               # 依赖库
└── README.md
```

## 数据文件格式

数据文件格式如下：
- 第一行：背包容量
- 后续行：每3行为一个项集，每行格式为 "重量 价值"

示例：
```
100.0
45.2 78.5
32.1 56.3
50.0 85.0
```

## 编译和运行

### 方式一：使用批处理脚本（推荐）

**编译项目：**
```bash
双击运行 compile.bat
```

**运行主程序（GUI界面）：**
```bash
双击运行 run.bat
```

**运行测试程序（控制台测试）：**
```bash
双击运行 test.bat
```

### 方式二：使用命令行

**编译项目：**
```bash
javac -d bin -encoding UTF-8 -sourcepath src/main/java src/main/java/com/knapsack/ui/MainFrame.java
```

**运行主程序：**
```bash
java -cp bin com.knapsack.ui.MainFrame
```

**运行测试程序：**
```bash
java -cp bin com.knapsack.KnapsackTest
```

### 方式三：使用Eclipse IDE

详细说明请参考 [Eclipse使用指南.md](docs/Eclipse使用指南.md)

**快速步骤：**
1. 在Eclipse中导入项目：`File` → `Import` → `Existing Projects into Workspace`
2. 选择项目目录：`F:\softwareproject`
3. 运行主程序：右键 `MainFrame.java` → `Run As` → `Java Application`
4. 运行测试：右键 `KnapsackTest.java` → `Run As` → `Java Application`

## 使用说明

1. **打开数据文件**：点击菜单"文件" -> "打开数据文件"，选择数据文件
2. **查看数据**：在数据列表中查看加载的项集数据
3. **排序数据**：点击"按价值/重量比排序"按钮对数据进行排序
4. **显示散点图**：点击"显示散点图"按钮查看数据可视化
5. **求解最优解**：点击"求解最优解"按钮计算最优解
6. **导出结果**：点击"导出TXT"或"导出CSV"按钮保存求解结果

## 代码规范

本项目严格遵循《阿里巴巴Java开发手册》和《腾讯C++编码规范》，详见 [代码规范说明.md](docs/代码规范说明.md)

## 技术栈

- Java SE 8+
- Swing (GUI框架)
- 动态规划算法

## 作者

软件工程实验项目

## 许可证

本项目仅用于教学目的
