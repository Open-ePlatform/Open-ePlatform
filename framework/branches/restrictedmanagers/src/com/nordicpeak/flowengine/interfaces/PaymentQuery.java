package com.nordicpeak.flowengine.interfaces;

import java.util.List;

public interface PaymentQuery extends QueryInstance {

	List<? extends InvoiceLine> getInvoiceLines();

}
