public class personInvoccationHandler implements InvocationHandler{
    person per;
    public personInvoccationHandler(person per) {
        this.per = per;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Hi!");
        return null;
    }
}