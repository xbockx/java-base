package staticproxy;

import staticproxy.agency.Taobao;

public class Main {
    public static void main(String[] args) {
        Taobao taobao = new Taobao();
        float price = taobao.sell(1);
        System.out.println("共: " + price + "元");
    }
}
