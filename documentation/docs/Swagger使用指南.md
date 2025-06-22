# Swagger使用指南

## 1. 简介
Swagger（现在称为OpenAPI）是一个强大的API文档工具，它可以帮助我们自动生成、可视化和管理API文档。在我们的项目中，我们使用了`springdoc-openapi`来实现Swagger功能。

## 2. 访问地址
- Swagger UI界面：`http://localhost:8080/swagger-ui.html`
- OpenAPI规范文档：`http://localhost:8080/v3/api-docs`

## 3. 常用注解说明

### 3.1 类级别注解
```java
@Tag(name = "模块名称", description = "模块描述")
```
用于Controller类上，对整个模块进行分组和描述。

### 3.2 方法级别注解
```java
@Operation(summary = "接口简述", description = "详细描述")
```
用于Controller方法上，描述API接口的功能。

### 3.3 参数注解
```java
@Parameter(description = "参数描述", required = true)
```
用于方法参数上，描述请求参数。

### 3.4 实体类注解
```java
@Schema(description = "实体描述")
```
用于实体类和字段上，描述数据模型。

## 4. 完整示例

### 4.1 Controller示例
```java
@RestController
@RequestMapping("/api/kafka")
@Tag(name = "Kafka消息", description = "Kafka消息发送和接收接口")
public class KafkaController {

    @Operation(summary = "发送消息", description = "向指定的Kafka主题发送消息")
    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(
            @Parameter(description = "主题名称", required = true) 
            @RequestParam String topic,
            
            @Parameter(description = "消息内容", required = true) 
            @RequestBody String message) {
        // 实现逻辑
        return ResponseEntity.ok("消息发送成功");
    }

    @Operation(summary = "获取消息列表")
    @GetMapping("/messages")
    public ResponseEntity<List<String>> getMessages(
            @Parameter(description = "主题名称", required = true) 
            @RequestParam String topic,
            
            @Parameter(description = "获取的消息数量", required = false) 
            @RequestParam(defaultValue = "10") Integer limit) {
        // 实现逻辑
        return ResponseEntity.ok(new ArrayList<>());
    }
}
```

### 4.2 实体类示例
```java
@Schema(description = "消息实体")
public class Message {
    
    @Schema(description = "消息ID", example = "123")
    private String id;
    
    @Schema(description = "消息内容", example = "Hello World")
    private String content;
    
    @Schema(description = "发送时间", example = "2024-01-01 12:00:00")
    private LocalDateTime sendTime;
    
    // getter和setter
}
```

## 5. 配置说明

### 5.1 Maven依赖
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-ui</artifactId>
    <version>1.6.15</version>
</dependency>
```

### 5.2 application.yml配置
```yaml
springdoc:
  swagger-ui:
    path: /swagger-ui.html    # Swagger UI的访问路径
  api-docs:
    path: /v3/api-docs       # OpenAPI文档的访问路径
  packages-to-scan: com.example   # 扫描的包路径
  show-actuator: false       # 是否展示Actuator的端点
```

## 6. 最佳实践

1. **合理分组**：使用`@Tag`注解对API进行分组，使文档结构清晰。

2. **详细描述**：
   - 为每个接口提供清晰的summary和description
   - 为所有参数添加描述信息
   - 为响应结果添加示例

3. **示例值**：
   - 使用`@Schema`的example属性提供示例值
   - 示例值应该是有意义的，能帮助调用者理解

4. **响应文档**：
   - 使用`@ApiResponse`描述可能的响应情况
   - 包括成功和失败的情况

5. **版本控制**：
   - 在OpenAPI配置中指定版本信息
   - 在有重大变更时更新版本号

## 7. 常见问题

1. **接口不显示**
   - 检查Controller是否有`@RestController`注解
   - 检查包路径是否在扫描范围内
   - 检查方法是否有对应的Mapping注解

2. **参数描述不显示**
   - 检查`@Parameter`注解是否正确使用
   - 检查实体类的`@Schema`注解

3. **无法访问Swagger UI**
   - 检查应用是否正常启动
   - 确认访问地址是否正确
   - 检查配置文件中的路径设置

## 8. 注意事项

1. 生产环境建议关闭Swagger或设置访问权限
2. 定期更新文档，保持与代码的同步
3. 对于敏感信息（如密码等），注意在示例中使用占位符
4. 文档要简洁但不失完整性 