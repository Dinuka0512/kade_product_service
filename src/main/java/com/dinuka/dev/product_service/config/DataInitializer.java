package com.dinuka.dev.product_service.config;

import com.dinuka.dev.product_service.model.Category;
import com.dinuka.dev.product_service.model.Product;
import com.dinuka.dev.product_service.model.Vendor;
import com.dinuka.dev.product_service.repository.CategoryRepository;
import com.dinuka.dev.product_service.repository.ProductRepository;
import com.dinuka.dev.product_service.repository.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;
    private final ProductRepository productRepository;

    public DataInitializer(CategoryRepository categoryRepository, VendorRepository vendorRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        if (categoryRepository.count() > 0) return;

        Category catElectronics = categoryRepository.save(new Category("Electronics", "electronics", "\uD83D\uDCF1"));
        Category catFashion = categoryRepository.save(new Category("Fashion", "fashion", "\uD83D\uDC55"));
        Category catHome = categoryRepository.save(new Category("Home & Living", "home-living", "\uD83C\uDFE0"));
        Category catGroceries = categoryRepository.save(new Category("Groceries", "groceries", "\uD83D\uDED2"));
        Category catBeauty = categoryRepository.save(new Category("Beauty", "beauty", "✨"));
        Category catSports = categoryRepository.save(new Category("Sports & Outdoors", "sports", "⚽"));

        String img = "https://picsum.photos/seed/%s/800/800";
        String img200 = "https://picsum.photos/seed/%s/200/200";
        String img1200 = "https://picsum.photos/seed/%s/1200/1200";

        Vendor v1 = vendorRepository.save(createVendor("Colombo Digital Hub", "colombo-digital-hub",
                "Gadgets that keep you ahead",
                "Colombo Digital Hub sources the latest electronics and gadgets from trusted global brands, with a focus on warranty-backed products and fast island-wide delivery.",
                img200.formatted("vendor-1-logo"), img1200.formatted("vendor-1-cover"),
                "Colombo 03", 4.7, 342, LocalDate.of(2024, 3, 12)));

        Vendor v2 = vendorRepository.save(createVendor("Ceylon Threads", "ceylon-threads",
                "Handcrafted apparel with a local touch",
                "Ceylon Threads is a family-run studio in Galle producing contemporary clothing from premium Sri Lankan fabrics.",
                img200.formatted("vendor-2-logo"), img1200.formatted("vendor-2-cover"),
                "Galle", 4.9, 187, LocalDate.of(2023, 11, 2)));

        Vendor v3 = vendorRepository.save(createVendor("Island Home", "island-home",
                "Comfort for every corner of your home",
                "From aromatic candles to furniture, Island Home curates practical, beautiful products for modern Sri Lankan households.",
                img200.formatted("vendor-3-logo"), img1200.formatted("vendor-3-cover"),
                "Kandy", 4.5, 96, LocalDate.of(2024, 6, 20)));

        Vendor v4 = vendorRepository.save(createVendor("Fresh Market SL", "fresh-market-sl",
                "Farm to doorstep in 24 hours",
                "Fresh Market SL partners with smallholder farmers across the island to deliver certified organic groceries.",
                img200.formatted("vendor-4-logo"), img1200.formatted("vendor-4-cover"),
                "Dambulla", 4.6, 521, LocalDate.of(2023, 8, 15)));

        productRepository.save(createProduct("Aurora Wireless Headphones", "aurora-wireless-headphones",
                "Active noise cancelling over-ear headphones with 40h battery life.",
                "Immerse yourself in studio-grade sound with the Aurora Wireless Headphones.",
                68500, 82000.0,
                img.formatted("aurora-headphones") + "," + img.formatted("aurora-headphones-2") + "," + img.formatted("aurora-headphones-3"),
                catElectronics.getId(), v1.getId(), 24, 312, 4.8, 210, true, "audio,wireless,noise-cancelling"));

        productRepository.save(createProduct("Swift 65W GaN Charger", "swift-65w-gan-charger",
                "Ultra-compact dual-port charger for phones, tablets and laptops.",
                "Charge everything from one pocket-sized brick.",
                12500, null,
                img.formatted("gan-charger") + "," + img.formatted("gan-charger-2"),
                catElectronics.getId(), v1.getId(), 60, 428, 4.5, 88, false, "charging,travel,gan"));

        productRepository.save(createProduct("Nimbus Smart Watch", "nimbus-smart-watch",
                "AMOLED display, heart-rate tracking and 10-day battery.",
                "The Nimbus Smart Watch blends a vibrant AMOLED display with serious fitness tracking.",
                45000, 56000.0,
                img.formatted("smart-watch") + "," + img.formatted("smart-watch-2") + "," + img.formatted("smart-watch-3"),
                catElectronics.getId(), v1.getId(), 15, 197, 4.6, 134, true, "wearables,fitness,smartwatch"));

        productRepository.save(createProduct("Linen Resort Shirt", "linen-resort-shirt",
                "Breathable 100% linen shirt in a relaxed fit.",
                "Cut from washed European linen, this resort shirt keeps you cool in tropical heat.",
                8900, 11500.0,
                img.formatted("linen-shirt") + "," + img.formatted("linen-shirt-2"),
                catFashion.getId(), v2.getId(), 40, 254, 4.9, 156, true, "linen,summer,shirt"));

        productRepository.save(createProduct("Batik Sarong", "batik-sarong",
                "Hand-dyed batik sarong, made to order.",
                "Each batik sarong is hand-dyed in Galle using traditional wax-resist techniques.",
                5400, null,
                img.formatted("batik-sarong") + "," + img.formatted("batik-sarong-2"),
                catFashion.getId(), v2.getId(), 18, 138, 5.0, 42, false, "batik,handmade,local"));

        productRepository.save(createProduct("Canvas Tote - Coconut Grove", "canvas-tote-coconut-grove",
                "Heavyweight canvas tote with screen-printed art.",
                "A sturdy 12oz canvas tote featuring original screen-printed artwork.",
                3200, null,
                img.formatted("canvas-tote") + "," + img.formatted("canvas-tote-2"),
                catFashion.getId(), v2.getId(), 75, 486, 4.7, 63, false, "tote,canvas,eco"));

        productRepository.save(createProduct("Cinnamon Cedar Candle", "cinnamon-cedar-candle",
                "Hand-poured soy candle, 45h burn time.",
                "Hand-poured in small batches from soy wax.",
                2600, 3200.0,
                img.formatted("candle") + "," + img.formatted("candle-2"),
                catHome.getId(), v3.getId(), 90, 612, 4.8, 205, true, "candle,aroma,home"));

        productRepository.save(createProduct("Teak Serving Board", "teak-serving-board",
                "Reclaimed teak board for serving and chopping.",
                "Crafted from reclaimed railway teak.",
                7800, null,
                img.formatted("teak-board") + "," + img.formatted("teak-board-2"),
                catHome.getId(), v3.getId(), 12, 94, 4.6, 31, false, "teak,kitchen,reclaimed"));

        productRepository.save(createProduct("Rattan Lounge Chair", "rattan-lounge-chair",
                "Handwoven rattan chair with plush cushion.",
                "A statement piece woven by village artisans.",
                47500, 59000.0,
                img.formatted("rattan-chair") + "," + img.formatted("rattan-chair-2") + "," + img.formatted("rattan-chair-3"),
                catHome.getId(), v3.getId(), 6, 57, 4.9, 27, true, "furniture,rattan,handmade"));

        productRepository.save(createProduct("Organic Ceylon Tea Sampler", "organic-ceylon-tea-sampler",
                "5 estate teas from the hill country, 250g.",
                "A curated journey through Sri Lanka's tea country.",
                4200, null,
                img.formatted("tea-sampler") + "," + img.formatted("tea-sampler-2"),
                catGroceries.getId(), v4.getId(), 120, 1043, 4.9, 318, true, "tea,organic,gift"));

        productRepository.save(createProduct("King Coconut Set (6)", "king-coconut-set-6",
                "Farm-fresh king coconuts, delivered chilled.",
                "Six tender king coconuts harvested at dawn.",
                1800, null,
                img.formatted("king-coconut") + "," + img.formatted("king-coconut-2"),
                catGroceries.getId(), v4.getId(), 45, 835, 4.7, 429, false, "coconut,fresh,organic"));

        productRepository.save(createProduct("Riptide Fitness Bottle", "riptide-fitness-bottle",
                "750ml insulated steel bottle, 24h cold.",
                "Double-wall vacuum insulated steel keeps drinks cold for 24 hours.",
                6500, 7900.0,
                img.formatted("fitness-bottle") + "," + img.formatted("fitness-bottle-2"),
                catSports.getId(), v1.getId(), 55, 341, 4.6, 74, false, "bottle,fitness,insulated"));
    }

    private Vendor createVendor(String name, String slug, String tagline, String description,
                                 String logo, String cover, String location,
                                 double rating, int reviewCount, LocalDate joinedAt) {
        Vendor v = new Vendor();
        v.setName(name);
        v.setSlug(slug);
        v.setTagline(tagline);
        v.setDescription(description);
        v.setLogo(logo);
        v.setCover(cover);
        v.setLocation(location);
        v.setRating(rating);
        v.setReviewCount(reviewCount);
        v.setJoinedAt(joinedAt);
        v.setStatus("active");
        return v;
    }

    private Product createProduct(String name, String slug, String shortDesc, String longDesc,
                                   double price, Double compareAtPrice, String images,
                                   Long categoryId, Long vendorId, int stock, int soldCount,
                                   double rating, int reviewCount, boolean isFeatured, String tags) {
        Product p = new Product();
        p.setName(name);
        p.setSlug(slug);
        p.setShortDescription(shortDesc);
        p.setLongDescription(longDesc);
        p.setPrice(price);
        p.setCompareAtPrice(compareAtPrice);
        p.setImagesRaw(images);
        p.setCategoryId(categoryId);
        p.setVendorId(vendorId);
        p.setStock(stock);
        p.setSoldCount(soldCount);
        p.setRating(rating);
        p.setReviewCount(reviewCount);
        p.setFeatured(isFeatured);
        p.setTagsRaw(tags);
        return p;
    }
}
