package staticproxy.agency;

import staticproxy.factory.Samsung;
import staticproxy.service.SellingBusiness;

public class Taobao implements SellingBusiness {
    private Samsung samsung = new Samsung();
    @Override
    public float sell(int amount) {
        float price = samsung.sell(amount);
        System.out.println("增: 碎屏险 * 1");
        return price + 100;
    }
}
