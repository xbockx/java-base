package dynamicproxy.factory;

import staticproxy.service.SellingBusiness;

public class Huawei implements SellingBusiness {
    @Override
    public float sell(int amount) {
        return 3799 * amount;
    }
}
