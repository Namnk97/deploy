package com.nextsol.khangbb.constant;

import java.util.*;

public class Constant {
    private Constant() {
    }

    public static final Map<String, String> TYPE_PAYMENT_SLIP = new HashMap<>() {
    };
    public static final List<String> REPORT_BILL_CODE = new ArrayList<>();

    static {
        TYPE_PAYMENT_SLIP.put("Thu khác", "TK");
        TYPE_PAYMENT_SLIP.put("Chi Khác", "CK");
        TYPE_PAYMENT_SLIP.put(Constant.GET_ORDER, "TDH");
        TYPE_PAYMENT_SLIP.put(Constant.PAY_ORDER, "CDH");
        REPORT_BILL_CODE.add("Nhập trả hàng");
        REPORT_BILL_CODE.add("Thu tiền bán lẻ");
        REPORT_BILL_CODE.add("Thu tiền bán sỉ");
    }

    public static final String SUPER_ADMIN = "VIP";
    public static final Long SUPER_ADMIN_ID = (long) 3;
    public static final Long USER_ID = (long) 2;

    public static final String UPDATE_FAIL = "Cập nhật thông tin thất bại";
    public static final String[] TYPE_REASON = {"CDC", "CCC", "DH", "XB", "PNT", "TDC", "PNT"};
    public static final String SPEND_TYPE = "Chi tiền";
    public static final String SPEND_END_SHIFT = "Chi cuối ca";
    public static final String PAY_ORDER = "Chi trả phiếu đặt hàng";
    public static final String REPAY_ORDER = "Chi đặt hàng";
    public static final String COLLECT_TYPE = "Thu tiền";
    public static final String GET_ORDER = "Thu đặt hàng";
    public static final String COLLECT_ADJUST_SAFE = "Thu điều chỉnh";
    public static final String SPEND_ADJUST_SAFE = "Chi điều chỉnh";
    public static final String CASH_PAYMENT_TYPE = "Tiền mặt";
    public static final String TRANSFER_PAYMENT_TYPE = "Chuyển khoản";
    public static final String VNPAY_PAYMENT_TYPE = "VNPay";
    public static final String CARD_PAYMENT_TYPE = "Thẻ";
    public static final String DEBIT_PAYMENT_TYPE = "Ghi nợ";

    public static final List<String> PARENT_KEY_SCREEN = new LinkedList<>() {{
        add("root");
        add("sell-product");
        add("warehouse-root"); // Kho
        add("money-root"); // tiền
        add("customer-root"); // nhà cung cấp - khách hàng
        add("reporter-root"); // báo cáo
        add("settings-root"); // thiết lập
    }};

    public static final Map<String, List<String>> KEY_SCREEN = new HashMap<>() {{
        put("root", new LinkedList<>());
        put("sell-product", new LinkedList<>() {{
            add("sell-product-");
        }});
        put("warehouse-root", new LinkedList<>() {{
            add("#nhap-kho");
            add("#nhap-tra-hang");
            add("#dieu-chuyen");
            add("#xuat-tra-hang");
            add("#kiem-ke");
            add("#dieu-chuyen-lien-chi-nhanh");
            add("warehouse");
        }});
        put("money-root", new LinkedList<>() {{
            add("#thu-chi");
            add("#ket-ca");
            add("#ls-ket-ca");
            add("#dieu-chinh-tien");
            add("money");
        }});
        put("customers-root", new LinkedList<>() {{
            add("customers");
            add("customers-group");
            add("supplier"); // Nhà cung cấp
            add("supplier-group");
        }});

        put("settings-root", new LinkedList<>() {{
            add("#categories"); // nhóm sản phẩm
            add("#warehouses"); // danh sách kho
            add("#products"); // Sản phẩm
            add("#accounts"); // taì khoản
            add("#branch"); // Chi nhánh
            add("#product-stamps"); // In tem sản phẩm
            add("#unit"); // Đơn vị tính
            add("#promotion"); // Tích điểm/Khuyến mãi
            add("#warranty"); //Quản lý bảo hành
            add("#invoice"); //Thông tin hóa đơn
            add("#stamps-setting"); // Thiết lập tem sản phẩm
            add("#print-setting"); //Thiết lập máy in
            add("#synchronous-setting"); // Cấu hình đồng bộ
            add("#general-setting"); // Thiết lập chung
            add("#invoice-printing-template"); // Thiết lập mẫu in hóa đơn
            add("#delete-data"); // Xóa dữ liệu
            add("#SMS"); // Cấu hình SMS
            add("settings");
        }});

        put("reporter-root", new LinkedList<>() {{
            add("invoice-sales"); // Bán hàng theo hóa đơn
            add("inventory"); //Tồn kho
            add("employee-sales"); // Bán hàng theo nhân viên
            add("product-sales"); // Bán hàng theo sản phẩm
            add("stock-import"); // Nhập kho
            add("product-profit"); // Lợi nhuận theo sản phẩm
            add("time-sales"); // Bán hàng theo thời gian
            add("stock-export"); // XUất kho
            add("overall-profit"); // Lợi nhuận tổng quan
            add("order-profit"); // Lợi nhuận theo hóa đơn
            add("selling-diary"); // Nhật ký bán hàng
            add("entry-exist"); // Nhập xuất tồn
            add("purchase-supplier"); // Nhập mua theo NCC
            add("promotional-points"); //Tích điểm khuyến mãi
            add("inventory-product-group"); // Tồn kho theo nhóm sản phẩm
            add("sales-details-according-invoice"); //Chi tiết bán hàng theo hóa đơn
            add("general-report-import-export"); // Báo cáo tổng hợp nhập/xuất kho
            add("report-canceled-invoice"); // Báo cáo hóa đơn đã hủy
            add("report-customer-debit"); // Báo cáo công nợ khách hàng theo hóa đơn bán hàng
        }});
    }};
}
