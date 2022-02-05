package staticproxy.factory;

import staticproxy.service.SellingBusiness;

public class Samsung implements SellingBusiness {
    @Override
    public float sell(int amount) {
        return 4799 * amount;
    }
}
