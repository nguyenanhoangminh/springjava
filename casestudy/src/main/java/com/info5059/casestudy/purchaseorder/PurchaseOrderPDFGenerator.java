package com.info5059.casestudy.purchaseorder;

import com.info5059.casestudy.vendor.Vendor;
import com.info5059.casestudy.vendor.VendorRepository;
import com.info5059.casestudy.product.Product;
import com.info5059.casestudy.product.ProductRepository;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.layout.element.Table;
//import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import org.springframework.web.servlet.view.document.AbstractPdfView;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URL;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalDateTime;

/**
 * PurchaseOrderPDFGenerator - a class for creating dynamic expense report
 * output in PDF format using the iText 7 library
 *
 * @author Evan
 */
public abstract class PurchaseOrderPDFGenerator extends AbstractPdfView {
    public static ByteArrayInputStream generateReport(String poid, PurchaseOrderDAO poDAO,
            VendorRepository vendorRepository, ProductRepository productRepository) throws IOException {
        URL imageUrl = PurchaseOrderPDFGenerator.class.getResource("/static/images/logo.png");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        // Initialize PDF document to be written to a stream not a file
        PdfDocument pdf = new PdfDocument(writer);
        // Document is the main object
        Document document = new Document(pdf);
        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        // add the image to the document
        Image img = new Image(ImageDataFactory.create(imageUrl)).scaleAbsolute(120, 40).setFixedPosition(80, 710);
        document.add(img);
        // now let's add a big heading
        document.add(new Paragraph("\n\n"));
        Locale locale = new Locale("en", "US");
        NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
        try {
            PurchaseOrder po = poDAO.findOne(Long.parseLong(poid));
            document.add(new Paragraph(String.format("Purchase Order")).setFont(font).setFontSize(24).setMarginRight(75)
                    .setTextAlignment(TextAlignment.RIGHT).setBold());
            document.add(new Paragraph("#" + poid).setFont(font).setFontSize(16).setBold().setMarginRight(90)
                    .setMarginTop(-10).setTextAlignment(TextAlignment.RIGHT));
            Optional<Vendor> opt = vendorRepository.findById(po.getVendorid());
            if (opt.isPresent()) {
                Vendor vendor = opt.get();
                document.add(new Paragraph("Vendor#:").setFont(font).setFontSize(13).setBold());
                Table vendorTable = new Table(1);
                vendorTable.setWidth(new UnitValue(UnitValue.PERCENT, 25));
                Cell vendorCell = new Cell()
                        .add(new Paragraph(vendor.getName()).setFont(font).setFontSize(12).setBold())
                        .setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT)
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY);
                vendorTable.addCell(vendorCell);
                vendorCell = new Cell().add(new Paragraph(vendor.getAddress1()).setFont(font).setFontSize(12).setBold())
                        .setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT)
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY);
                vendorTable.addCell(vendorCell);
                vendorCell = new Cell().add(new Paragraph(vendor.getCity()).setFont(font).setFontSize(12).setBold())
                        .setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT)
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY);
                vendorTable.addCell(vendorCell);
                vendorCell = new Cell().add(new Paragraph(vendor.getProvince()).setFont(font).setFontSize(12).setBold())
                        .setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT)
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY);
                vendorTable.addCell(vendorCell);
                vendorCell = new Cell().add(new Paragraph(vendor.getEmail()).setFont(font).setFontSize(12).setBold())
                        .setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT)
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY);
                vendorTable.addCell(vendorCell);
                document.add(vendorTable.setMarginLeft(57));
                document.add(new Paragraph("\n\n"));
            }

            // dump out the line items
            BigDecimal subTot = new BigDecimal(0.0);
            BigDecimal tax = new BigDecimal(0.0);
            BigDecimal tot = new BigDecimal(0.0);
            // now a 5 column table
            Table productTable = new Table(new float[] { 190, 160, 80, 80, 110 });
            // table headings
            Cell cell = new Cell().add(new Paragraph("Product Code").setFont(font).setFontSize(12).setBold())
                    .setTextAlignment(TextAlignment.CENTER);
            productTable.addCell(cell);
            cell = new Cell().add(new Paragraph("Description").setFont(font).setFontSize(12).setBold())
                    .setTextAlignment(TextAlignment.CENTER);
            productTable.addCell(cell);
            cell = new Cell().add(new Paragraph("Qty Sold").setFont(font).setFontSize(12).setBold())
                    .setTextAlignment(TextAlignment.CENTER);
            productTable.addCell(cell);
            cell = new Cell().add(new Paragraph("Price").setFont(font).setFontSize(12).setBold())
                    .setTextAlignment(TextAlignment.CENTER);
            productTable.addCell(cell);
            cell = new Cell().add(new Paragraph("Ext. Price").setFont(font).setFontSize(12).setBold())
                    .setTextAlignment(TextAlignment.CENTER);
            productTable.addCell(cell);
            for (PurchaseOrderLineitem lineitem : po.getItems()) {
                // System.out.println("Line Item: " + lineitem + "\n\n\n\n\n\n\n\n");
                // System.out.println("Line Item Product ID: " + lineitem.getProductid() + "\n\n\n\n\n\n\n\n");
                
                // var products = productRepository.findAll();
                // for (Product product : products) {
                //     System.out.println("Product ID: " + product.getId() + " : " + lineitem.getProductid() + "\n");
                //     if (product.getId() == lineitem.getProductid()) {
                //         System.out.println("MATCH!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + "\n");
                //     }
                // }
                Optional<Product> optx = productRepository.findById(lineitem.getProductid());
                if (optx.isPresent()) {
                    System.out.println("optx.isPresent " + lineitem.getProductid() + "\n\n\n\n\n\n\n\n");
                    Product product = optx.get();
                    // price of each product
                    BigDecimal itemPrice = new BigDecimal(0.0);
                    itemPrice = product.getCostprice().multiply(BigDecimal.valueOf(lineitem.getQty()));
                    subTot = subTot.add(itemPrice, new MathContext(8, RoundingMode.UP));
                    cell = new Cell().add(new Paragraph((product.getId()))).setTextAlignment(TextAlignment.CENTER);
                    productTable.addCell(cell);
                    cell = new Cell().add(new Paragraph(product.getName())).setTextAlignment(TextAlignment.CENTER);
                    productTable.addCell(cell);
                    cell = new Cell().add(new Paragraph(Integer.toString(lineitem.getQty())))
                            .setTextAlignment(TextAlignment.CENTER);
                    productTable.addCell(cell);
                    cell = new Cell().add(new Paragraph(formatter.format(product.getCostprice())))
                            .setTextAlignment(TextAlignment.RIGHT);
                    productTable.addCell(cell);
                    cell = new Cell().add(new Paragraph(formatter.format(itemPrice)))
                            .setTextAlignment(TextAlignment.RIGHT);
                    productTable.addCell(cell);
                }
            }

            // purchase order total
            tax = subTot.multiply(BigDecimal.valueOf(13)).divide(BigDecimal.valueOf(100));
            tot = subTot.add(tax);

            cell = new Cell(1, 4).add(new Paragraph("Sub Total:")).setBorder(Border.NO_BORDER)
                    .setTextAlignment(TextAlignment.RIGHT);
            productTable.addCell(cell);
            cell = new Cell().add(new Paragraph(formatter.format(subTot))).setTextAlignment(TextAlignment.RIGHT);
            productTable.addCell(cell);

            cell = new Cell(1, 4).add(new Paragraph("Tax:")).setBorder(Border.NO_BORDER)
                    .setTextAlignment(TextAlignment.RIGHT);
            productTable.addCell(cell);
            cell = new Cell().add(new Paragraph(formatter.format(tax))).setTextAlignment(TextAlignment.RIGHT);
            productTable.addCell(cell);

            cell = new Cell(1, 4).add(new Paragraph("Total:")).setBorder(Border.NO_BORDER)
                    .setTextAlignment(TextAlignment.RIGHT);
            productTable.addCell(cell);
            cell = new Cell().add(new Paragraph(formatter.format(tot))).setTextAlignment(TextAlignment.RIGHT)
                    .setBackgroundColor(ColorConstants.YELLOW);
            productTable.addCell(cell);
            document.add(productTable);
            document.add(new Paragraph("\n\n"));
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a");
            document.add(
                    new Paragraph(dateFormatter.format(LocalDateTime.now())).setTextAlignment(TextAlignment.CENTER));
            document.close();
        } catch (Exception ex) {
            Logger.getLogger(PurchaseOrderPDFGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        // finally send stream back to the controller
        return new ByteArrayInputStream(baos.toByteArray());
    }
}