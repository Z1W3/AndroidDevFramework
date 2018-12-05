# AndroidDevelopmentFramework
- 整个框架采用MVP的设计模式
- 创建的每个Activity、Fragment、DialogFragment均需要继承 `catt.mvp.sample.base.app.*` 包内的基础类
- 每个父类为 `catt.mvp.sample.base.app.*` 的子类均需实现一个该子类的代理实现类
- 每个代理实现类均需要继承 `catt.mvp.sample.base.proxy.*` 该包内的 ProxyBase父类
- 每个父类为 `catt.mvp.sample.base.proxy.*` 的子类均需传递三个泛型类型 `对应绑定的Activity、Fragment或DialogFragment的组件` , `View层的Interface` , `绑定的Presenter类`
- 每个父类为 `catt.mvp.sample.base.proxy.*` 的子类均需引用自身泛型中的View层Interface
- 每个被绑定的Presenter均需引用对应的Presenter层Interface
- 每个对应的View <-> Presenter 需要定义一个公共的接口,每个公共的接口中需要两个子接口 `View层Interface`与`Presenter层Interface`
- 每个代理实现类的消息传递可以采用 事件总线 `EventBus` 进行实现