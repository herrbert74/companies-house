import shared

//func simpleChildStack<T : AnyObject>(_ child: T) -> Value<ChildStack<AnyObject, T>> {
//    return mutableValue(
//        ChildStack(
//            configuration: "config" as AnyObject,
//            instance: child
//        )
//    )
//}

func simpleChildStack<T : AnyObject> (_ child: T) -> Value<ChildStack<Configuration, T>> {
    return valueOf(
        ChildStack(
            active: ChildCreated(
                configuration: Configuration.Main(),
                instance: child
            ),
            backStack: []
        )
    )
}
