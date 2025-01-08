package com.payvang.Invoice.Services;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.payvang.Invoice.Entities.Invoice;
import com.payvang.Invoice.Util.ConfigurationManager;
import com.payvang.Invoice.Util.PropertyConstants;
import java.io.File;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendEmail(String receiver, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(ConfigurationManager.getProperty(PropertyConstants.senderEmailAddress));
            helper.setTo(receiver);
            helper.setSubject(subject);
            helper.setText(body, false); // false indicates plain text

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }

    @Async
    public void sendInvoiceEmail(Invoice invoice, String paymentUrl) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            // Sender email
            helper.setFrom(ConfigurationManager.getProperty(PropertyConstants.senderEmailAddress));
            helper.setTo(invoice.getEmail());
            helper.setSubject("Invoice Payment: " + invoice.getInvoiceNo());

            // Load logo from resources
            FileSystemResource logoResource = new FileSystemResource(new File("src/main/resources/images/logo.png"));

            // HTML email content
            String emailContent = "<html>" +
                    "<body style='font-family: Arial, sans-serif; margin: 0; padding: 0;'>" +
                    "<div style='background-color: #f3f4f6; padding: 20px;'>" +
                    "<div style='max-width: 600px; margin: auto; background-color: #ffffff; padding: 30px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);'>" +
                    "<header style='text-align: center; margin-bottom: 30px;'>" +
                    "<img src='cid:logoImage' alt='Payvang Solutions Pvt. Ltd.' style='max-width: 200px; height: auto;'>" +
                    "<h2 style='color: #007bff; font-size: 24px; margin: 10px 0;'>Invoice Payment Request</h2>" +
                    "<p style='font-size: 16px; color: #555;'>Payvang Solutions Pvt. Ltd. - Your Trusted Payment Aggregator</p>" +
                    "</header>" +
                    "<p style='font-size: 16px; color: #333;'>Dear <strong>" + invoice.getName() + "</strong>,</p>" +
                    "<p style='font-size: 16px; color: #333;'>Thank you for your business with Payvang Solutions Pvt. Ltd. We have generated your invoice with the details below.</p>" +
                    "<table style='width: 100%; margin-top: 20px; border-collapse: collapse;'>" +
                    "<tr style='background-color: #f9f9f9;'>" +
                    "<th style='text-align: left; padding: 10px; border: 1px solid #ddd;'>Invoice No:</th>" +
                    "<td style='padding: 10px; border: 1px solid #ddd;'>" + invoice.getInvoiceNo() + "</td>" +
                    "</tr>" +
                    "<tr>" +
                    "<th style='text-align: left; padding: 10px; border: 1px solid #ddd;'>Total Amount:</th>" +
                    "<td style='padding: 10px; border: 1px solid #ddd;'>" + invoice.getCurrencyCode() + " " + invoice.getTotalAmount() + "</td>" +
                    "</tr>" +
                    "<tr style='background-color: #f9f9f9;'>" +
                    "<th style='text-align: left; padding: 10px; border: 1px solid #ddd;'>Due Date:</th>" +
                    "<td style='padding: 10px; border: 1px solid #ddd;'>" + invoice.getExpiresDay() + "</td>" +
                    "</tr>" +
                    "</table>" +
                    "<p style='font-size: 16px; color: #333; margin-top: 20px;'>Please click the button below to make the payment:</p>" +
                    "<div style='text-align: center; margin-top: 20px;'>" +
                    "<a href='" + paymentUrl + "' style='background-color: #007bff; color: white; padding: 12px 20px; text-decoration: none; border-radius: 5px; font-size: 18px;'>Pay Now</a>" +
                    "</div>" +
                    "<p style='font-size: 16px; color: #333; margin-top: 30px;'>If you have any questions or need assistance, please feel free to reach out to us at <a href='mailto:support@payvang.com' style='color: #007bff;'>support@payvang.com</a>.</p>" +
                    "<footer style='text-align: center; margin-top: 40px; font-size: 14px; color: #999;'>" +
                    "<p>&copy; 2025 Payvang Solutions Pvt. Ltd. All Rights Reserved.</p>" +
                    "</footer>" +
                    "</div>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            helper.setText(emailContent, true); // true indicates HTML content

            // Attach the logo image as inline
            helper.addInline("logoImage", logoResource);

            // Send the email
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send invoice email: " + e.getMessage(), e);
        }
    }

}
