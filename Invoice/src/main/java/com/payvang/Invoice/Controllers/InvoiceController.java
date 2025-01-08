package com.payvang.Invoice.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.payvang.Invoice.Entities.Invoice;
import com.payvang.Invoice.Services.EmailService;
import com.payvang.Invoice.Services.InvoiceService;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/create")
    public Invoice createInvoice(@RequestBody Invoice invoice) {
        return invoiceService.saveInvoice(invoice);
    }

    @PostMapping("/create-bulk")
    public List<Invoice> createBulkInvoices(@RequestBody List<Invoice> invoices) {
        return invoiceService.saveInvoices(invoices);
    }

    @PostMapping("/publish/{id}")
    public String publishInvoice(@PathVariable Long id) {
        Invoice invoice = invoiceService.getInvoiceById(id);

        if (invoice == null) {
            throw new RuntimeException("Invoice not found");
        }

        // Construct the payment URL (example URL)
        String paymentUrl = "https://payvang.com/invoice/pay/" + invoice.getInvoiceNo();

        emailService.sendInvoiceEmail(invoice, paymentUrl);
        return "Invoice published and email sent successfully!";
    }

    @GetMapping("/all")
    public List<Invoice> getAllInvoices() {
        return invoiceService.getAllInvoices();
    }
}