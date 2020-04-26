# statemachine
状态机demo

## 命令列表
### get 
获取当前角色生命值

### attack {damage:long}
攻击角色造成damage伤害值,状态变更有以下几种可能:
1. FULL-->HURT
2. FULL-->DEAD(过量伤害会造成直接死亡)
3. HURT-->HURT
4. HURT-->DEAD
5. DEAD-->DEAD(无尽的鞭尸??)

### heal {heal:long}
给角色加血，造成heal治疗量,状态变更有以下几种可能:
1. HURT-->FULL
2. HURT-->HURT(垃圾治疗，还加不满血!)

### reborn 
只有角色死亡了才可以重生
1. DEAD-->FULL
