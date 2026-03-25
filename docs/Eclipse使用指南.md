# Eclipse使用指南

## 一、在Eclipse中导入项目

### 方法1：直接导入现有项目（推荐）

1. **打开Eclipse**

2. **导入项目**
   - 点击菜单：`File` → `Import`
   - 选择：`General` → `Existing Projects into Workspace`
   - 点击 `Next`

3. **选择项目目录**
   - 在 `Select root directory` 中点击 `Browse`
   - 选择项目目录：`F:\softwareproject`
   - 勾选项目：`D01KnapsackProblem`
   - 点击 `Finish`

4. **验证导入成功**
   - 在 `Package Explorer` 中可以看到项目结构
   - 展开 `src/main/java` 可以看到所有Java文件
   - 项目没有红色错误标记

### 方法2：创建新项目并复制代码

1. **创建新Java项目**
   - 点击菜单：`File` → `New` → `Java Project`
   - 项目名称：`D01KnapsackProblem`
   - 点击 `Finish`

2. **创建包结构**
   - 右键点击 `src` → `New` → `Package`
   - 创建以下包：
     - `com.knapsack.model`
     - `com.knapsack.algorithm`
     - `com.knapsack.io`
     - `com.knapsack.ui`
     - `com.knapsack.util`

3. **复制源文件**
   - 从文件系统复制 `.java` 文件到对应的包中

## 二、配置项目

### 2.1 检查JDK版本

1. 右键点击项目 → `Properties`
2. 选择 `Java Compiler`
3. 确保 Compiler compliance level 为 1.8 或更高

### 2.2 配置源码路径

1. 右键点击项目 → `Properties`
2. 选择 `Java Build Path`
3. 点击 `Source` 标签
4. 确保包含：
   - `src/main/java`（源码目录）
   - `src/resources`（资源目录）

### 2.3 配置输出目录

1. 在 `Java Build Path` → `Source` 标签
2. 底部 `Default output folder` 设置为：`bin`

## 三、运行程序

### 3.1 运行主程序（GUI界面）

1. **找到主类**
   - 在 `Package Explorer` 中展开项目
   - 找到：`com.knapsack.ui` → `MainFrame.java`

2. **运行程序**
   - 右键点击 `MainFrame.java`
   - 选择：`Run As` → `Java Application`

3. **观察结果**
   - 程序会启动图形界面
   - 可以进行各种操作测试

### 3.2 运行测试程序（控制台测试）

1. **找到测试类**
   - 在 `Package Explorer` 中展开项目
   - 找到：`com.knapsack` → `KnapsackTest.java`

2. **运行测试**
   - 右键点击 `KnapsackTest.java`
   - 选择：`Run As` → `Java Application`

3. **查看输出**
   - 在 `Console` 视图中查看测试结果
   - 测试包括：
     - 数据模型类测试
     - 数据文件读取测试
     - 数据排序测试
     - 动态规划算法测试

## 四、调试程序

### 4.1 设置断点

1. 打开要调试的Java文件
2. 在代码行号左侧双击，设置断点
3. 断点位置会显示蓝色圆点

**推荐断点位置：**
- `DynamicProgrammingSolver.java` 第30行（算法入口）
- `DynamicProgrammingSolver.java` 第45行（状态转移）
- `DataFileReader.java` 第25行（文件读取）

### 4.2 启动调试

1. 右键点击 `KnapsackTest.java`
2. 选择：`Debug As` → `Java Application`
3. 程序会在断点处暂停

### 4.3 调试操作

- **F5**：单步进入（Step Into）
- **F6**：单步跳过（Step Over）
- **F7**：单步返回（Step Return）
- **F8**：继续执行（Resume）
- **Ctrl+F2**：终止调试

### 4.4 查看变量值

1. 在 `Variables` 视图中查看当前变量
2. 鼠标悬停在变量名上可以看到当前值
3. 在 `Expressions` 视图中可以添加监视表达式

## 五、测试功能详解

### 5.1 测试数据模型

```java
// 创建物品
Item item = new Item(0, 10.5, 25.0);
System.out.println("价值/重量比: " + item.getValueToWeightRatio());

// 创建项集
Item[] items = {item1, item2, item3};
ItemSet itemSet = new ItemSet(0, items);

// 创建背包问题
KnapsackProblem problem = new KnapsackProblem(100.0);
problem.addItemSet(itemSet);
```

### 5.2 测试文件读取

```java
DataFileReader reader = new DataFileReader();
KnapsackProblem problem = reader.readFile("src/resources/test_data_small.txt");
System.out.println("背包容量: " + problem.getCapacity());
System.out.println("项集数量: " + problem.getItemCount());
```

### 5.3 测试排序功能

```java
List<ItemSet> itemSets = problem.getItemSets();
DataSorter.sortByThirdItemValueToWeightRatio(itemSets);
// 查看排序后的结果
```

### 5.4 测试求解算法

```java
DynamicProgrammingSolver solver = new DynamicProgrammingSolver();
SolutionResult result = solver.solve(problem);
System.out.println("总价值: " + result.getTotalValue());
System.out.println("总重量: " + result.getTotalWeight());
System.out.println("求解时间: " + result.getSolveTime() + " ms");
```

## 六、常见问题解决

### 问题1：找不到或无法加载主类

**解决方案：**
1. 右键点击项目 → `Properties` → `Java Build Path`
2. 检查 `Source` 标签，确保 `src/main/java` 在列表中
3. 检查 `Default output folder` 是否为 `bin`
4. 点击 `Project` → `Clean...` 清理项目

### 问题2：中文乱码

**解决方案：**
1. 右键点击项目 → `Properties`
2. 选择 `Resource`
3. 设置 `Text file encoding` 为 `UTF-8`

### 问题3：找不到资源文件

**解决方案：**
1. 确保 `src/resources` 在 Build Path 中
2. 使用相对路径：`src/resources/test_data_small.txt`
3. 或使用类加载器：`getClass().getResourceAsStream("/test_data_small.txt")`

### 问题4：编译错误

**解决方案：**
1. 检查JDK版本是否正确
2. 点击 `Project` → `Clean...` 清理项目
3. 点击 `Project` → `Build Project` 重新编译
4. 检查是否有缺失的import语句

## 七、使用JUnit进行单元测试（可选）

### 7.1 添加JUnit库

1. 右键点击项目 → `Properties` → `Java Build Path`
2. 点击 `Libraries` 标签
3. 点击 `Add Library` → `JUnit` → `Next`
4. 选择 JUnit 4 或 JUnit 5
5. 点击 `Finish`

### 7.2 创建JUnit测试类

```java
package com.knapsack;

import org.junit.Test;
import static org.junit.Assert.*;

import com.knapsack.model.Item;

public class ItemTest {
    @Test
    public void testValueToWeightRatio() {
        Item item = new Item(0, 10.0, 25.0);
        assertEquals(2.5, item.getValueToWeightRatio(), 0.001);
    }
}
```

### 7.3 运行JUnit测试

1. 右键点击测试类
2. 选择：`Run As` → `JUnit Test`
3. 在 `JUnit` 视图中查看测试结果

## 八、性能分析

### 8.1 使用Eclipse Profiler

1. 右键点击 `KnapsackTest.java`
2. 选择：`Profile As` → `Java Application`
3. 在 `Profiling` 视图中查看性能数据

### 8.2 手动计时

```java
long startTime = System.currentTimeMillis();
// 执行代码
long endTime = System.currentTimeMillis();
System.out.println("耗时: " + (endTime - startTime) + " ms");
```

## 九、导出可运行JAR文件

### 9.1 创建JAR文件

1. 右键点击项目 → `Export`
2. 选择：`Java` → `Runnable JAR file`
3. 选择启动配置：`MainFrame`
4. 选择导出位置
5. 点击 `Finish`

### 9.2 运行JAR文件

```bash
java -jar D01KnapsackProblem.jar
```

## 十、快捷键

| 快捷键 | 功能 |
|--------|------|
| Ctrl+Shift+O | 自动导入包 |
| Ctrl+Shift+F | 格式化代码 |
| Ctrl+Space | 代码提示 |
| F3 | 跳转到声明 |
| Ctrl+Shift+T | 打开类型 |
| Ctrl+Shift+R | 打开资源 |
| Alt+Shift+R | 重命名 |
| Ctrl+/ | 注释/取消注释 |
| Ctrl+Shift+/ | 块注释 |
| Ctrl+Shift+\ | 取消块注释 |

## 十一、推荐设置

### 11.1 代码格式化

1. `Window` → `Preferences` → `Java` → `Code Style` → `Formatter`
2. 点击 `New` 创建新的格式化配置
3. 设置缩进为4个空格
4. 设置行宽为120字符

### 11.2 保存时自动格式化

1. `Window` → `Preferences` → `Java` → `Editor` → `Save Actions`
2. 勾选 `Perform the selected actions on save`
3. 勾选 `Format source code`

### 11.3 显示行号

1. `Window` → `Preferences` → `General` → `Editors` → `Text Editors`
2. 勾选 `Show line numbers`

## 十二、测试检查清单

- [ ] 项目成功导入Eclipse
- [ ] 没有编译错误
- [ ] 可以运行 `MainFrame`（GUI界面）
- [ ] 可以运行 `KnapsackTest`（控制台测试）
- [ ] 可以加载测试数据文件
- [ ] 可以进行调试
- [ ] 所有测试用例通过

## 十三、测试结果示例

运行 `KnapsackTest.java` 后，控制台输出应该类似：

```
========================================
D{0-1}背包问题求解器 - 测试程序
========================================

【测试1】数据模型类测试
----------------------------------------
创建物品: Item{id=0, weight=10.5, value=25.0}
价值/重量比: 2.3810
创建项集: ItemSet{id=0, items=[...]}
第三项价值/重量比: 2.3333
创建背包问题: KnapsackProblem{capacity=100.0, itemSetCount=1}
背包容量: 100.0
项集数量: 1
✓ 数据模型类测试通过

【测试2】数据文件读取测试
----------------------------------------
读取文件: src/resources/test_data_small.txt
背包容量: 50.0
项集数量: 3
...
✓ 数据文件读取测试通过

【测试3】数据排序测试
----------------------------------------
...
✓ 数据排序测试通过

【测试4】动态规划算法测试
----------------------------------------
...
✓ 动态规划算法测试通过

========================================
所有测试完成！
========================================
```

## 十四、获取帮助

如果遇到问题：

1. 检查 `Problems` 视图中的错误信息
2. 查看 `Console` 视图中的异常堆栈
3. 参考 [使用指南.md](使用指南.md)
4. 参考 [实验二_个人项目实验报告.md](实验二_个人项目实验报告.md)
