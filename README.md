# smp8583
8583报文解析工具，不依赖任何三方库

## 目前已完成
    
[x] ISO8584(无Header)    
[x] 带ASCII Header    
[x] 将报文拆解成field，field目前支持byte[]和ascii    

## 规划支持

[] 允许设置长度域模式，是否长度域值是否包含域本身(目前实现是包含)    
[] 支持TLV子域