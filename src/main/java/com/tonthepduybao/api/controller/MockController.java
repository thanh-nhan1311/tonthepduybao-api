package com.tonthepduybao.api.controller;

import com.google.gson.Gson;
import com.tonthepduybao.api.app.utils.DataBuilder;
import com.tonthepduybao.api.app.utils.TimeUtils;
import com.tonthepduybao.api.app.utils.Utils;
import com.tonthepduybao.api.entity.*;
import com.tonthepduybao.api.entity.enumeration.*;
import com.tonthepduybao.api.repository.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * MockController
 *
 * @author khal
 * @since 2022/12/24
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/ttdb/api/mock")
public class MockController {

    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final DebtRepository debtRepository;
    private final DebtDetailRepository debtDetailRepository;
    private final BranchRepository branchRepository;
    private final SiteContactRepository siteContactRepository;
    private final SiteCategoryRepository siteCategoryRepository;
    private final SitePartnerRepository sitePartnerRepository;
    private final SiteSettingRepository siteSettingRepository;
    private final PropertyRepository propertyRepository;
    private final ProductRepository productRepository;
    private final PropertyDetailRepository propertyDetailRepository;
    private final DebtDetailPropertyDetailRepository debtDetailPropertyDetailRepository;
    private final ProductPropertyDetailRepository productPropertyDetailRepository;
    private final DebtPropertyRepository debtPropertyRepository;

    private final InvoiceRepository invoiceRepository;
    private final ProductInvoiceRepository productInvoiceRepository;
    private final ShippingAddressRepository shippingAddressRepository;
    private final InvoiceShippingAddressRepository invoiceShippingAddressRepository;

    private final AuthSessionRepository authSessionRepository;

    @Transactional(rollbackFor = Throwable.class)
    @GetMapping(value = "{key}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> mock(@PathVariable("key") String key) {
        if (!key.equals("999638d9893a35e6be4f80cec500b334"))
            return ResponseEntity.badRequest().build();

        authSessionRepository.deleteAll();

        productInvoiceRepository.deleteAll();
        invoiceShippingAddressRepository.deleteAll();
        shippingAddressRepository.deleteAll();
        invoiceRepository.deleteAll();

        siteContactRepository.deleteAll();
        siteCategoryRepository.deleteAll();
        sitePartnerRepository.deleteAll();
        siteSettingRepository.deleteAll();

        debtPropertyRepository.deleteAll();
        debtDetailPropertyDetailRepository.deleteAll();
        productPropertyDetailRepository.deleteAll();
        propertyDetailRepository.deleteAll();
        propertyRepository.deleteAll();

        debtDetailRepository.deleteAll();
        debtRepository.deleteAll();

        productRepository.deleteAll();

        branchRepository.deleteAll();

        userRepository.deleteAll();
        roleRepository.deleteAll();

        customerRepository.deleteAll();

        List<Role> roles = mockRole();
        mockBranch();
        mockUser(roles.get(0));

        mockContact();
        mockSiteCategory();
        mockCustomer();
        mockProperty();

        mockCustomerShippingAddress();

        return ResponseEntity.ok("OK");
    }

    private void mockCustomerShippingAddress() {
        List<Customer> customers = customerRepository.findAll();
        List<ShippingAddress> shippingAddresses = new ArrayList<>();
        for (Customer customer : customers) {
            String name = customer.getName();
            String phone = customer.getPhone();
            String address = customer.getAddress();
            if (!StringUtils.hasLength(name) || !StringUtils.hasLength(phone) || !StringUtils.hasLength(address))
                continue;

            String defaultPhone = phone.split(",")[0];

            ShippingAddress shippingAddress = ShippingAddress.builder()
                    .name(name)
                    .phone(defaultPhone)
                    .defaultAddress(true)
                    .address(address)
                    .createdBy(1L)
                    .updatedBy(1L)
                    .createdAt(TimeUtils.nowStr())
                    .updatedAt(TimeUtils.nowStr())
                    .customer(customer)
                    .build();
            shippingAddresses.add(shippingAddress);
        }
        shippingAddressRepository.saveAll(shippingAddresses);
    }

    private void mockSiteCategory() {
        Faker faker = new Faker();

        for (var i = 0; i < 20; i++) {
            SiteCategory siteCategory = SiteCategory.builder()
                    .name(faker.company().name())
                    .seoUrl("-")
                    .parent(null)
                    .updatedAt(TimeUtils.nowStr())
                    .build();
            SiteCategory savedCategory =  siteCategoryRepository.saveAndFlush(siteCategory);
            String seoUrl = Utils.convertToSlug(savedCategory.getName()) + "-" + savedCategory.getId();
            savedCategory.setSeoUrl(seoUrl);
            siteCategoryRepository.saveAndFlush(savedCategory);
        }
    }

    private void mockContact() {
        Faker faker = new Faker();

        for (var i = 0; i < 50; i++) {
            SiteContact siteContact = SiteContact.builder()
                    .fullName(faker.name().fullName())
                    .phone(faker.phoneNumber().cellPhone())
                    .email(faker.internet().emailAddress())
                    .content(faker.lorem().sentence(20))
                    .resolvedFlag(false)
                    .createdAt(TimeUtils.nowStr())
                    .build();
            siteContactRepository.saveAndFlush(siteContact);
        }
    }

    private void mockCustomer() {
        User user = userRepository.findByUsernameAndDeleted("admin01", false).get();

        Gson gson = new Gson();
        InputStream inputStream = MockController.class.getClassLoader().getResourceAsStream("static/customers.json");
        if (Objects.isNull(inputStream)) return;

        CustomerJSON[] customers = gson.fromJson(new InputStreamReader(inputStream, StandardCharsets.UTF_8), CustomerJSON[].class);
        for (CustomerJSON customerJSON : customers) {
            Customer customer = new Customer();
            customer.setName(customerJSON.getName());
            customer.setPhone(customerJSON.getPhone());
            customer.setAddress(customerJSON.getAddress());
            customer.setCreatedBy(user.getId());
            customer.setUpdatedBy(user.getId());
            customer.setCreatedAt(TimeUtils.nowStr());
            customer.setUpdatedAt(TimeUtils.nowStr());
            customer.setType(ECustomerType.CUSTOMER);
            customerRepository.saveAndFlush(customer);
        }
    }

    private void mockUser(final Role adminRole) {
        String encodePassword = DigestUtils.md5DigestAsHex("123123123".getBytes());
        Stream.of(1L, 2L)
                .forEach(id -> {
                    User user = User.builder()
                            .id(id)
                            .username("admin0" + id)
                            .password(encodePassword)
                            .fullName("Admin 0" + id)
                            .status(EUserStatus.ACTIVE)
                            .createdAt(TimeUtils.nowStr())
                            .updatedAt(TimeUtils.nowStr())
                            .role(adminRole)
                            .deleted(false)
                            .build();
                    userRepository.saveAndFlush(user);
                });
    }

    private void mockBranch() {
        Gson gson = new Gson();
        InputStream inputStream = MockController.class.getClassLoader().getResourceAsStream("static/branch.json");
        if (Objects.isNull(inputStream)) return;

        BranchJSON[] branches = gson.fromJson(new InputStreamReader(inputStream, StandardCharsets.UTF_8), BranchJSON[].class);
        for (BranchJSON branchJSON : branches) {
            Branch branch = DataBuilder.to(branchJSON, Branch.class);
            branch.setCreatedAt(TimeUtils.nowStr());
            branch.setUpdatedAt(TimeUtils.nowStr());
            branch.setStatus(EBranchStatus.ACTIVE);
            branchRepository.saveAndFlush(branch);
        }
    }

    private void mockProperty() {
        Gson gson = new Gson();
        InputStream inputStream = MockController.class.getClassLoader().getResourceAsStream("static/props.json");
        if (Objects.isNull(inputStream)) return;

        User user = userRepository.findByUsernameAndDeleted("admin01", false)
                .orElseThrow();
        PropJSON[] props = gson.fromJson(new InputStreamReader(inputStream, StandardCharsets.UTF_8), PropJSON[].class);
        Stream.of(props)
                .forEach(prop -> {
                    Property property = Property.builder()
                            .name(prop.getName())
                            .type(EType.valueOf(prop.getType()))
                            .orderBy(prop.getOrder())
                            .createdBy(user.getId())
                            .updatedBy(user.getId())
                            .createdAt(TimeUtils.nowStr())
                            .updatedAt(TimeUtils.nowStr())
                            .build();

                    Property savedProperty = propertyRepository.saveAndFlush(property);
                    List<PropertyDetail> propertyDetail = Stream.of(prop.getValues().split(","))
                            .map(item -> PropertyDetail.builder()
                                    .name(item)
                                    .property(savedProperty)
                                    .build())
                            .toList();
                    propertyDetailRepository.saveAllAndFlush(propertyDetail);
                });
    }

    private List<Role> mockRole() {
        return Arrays.stream(ERole.values())
                .map(eRole -> {
                    Role role = Role.builder()
                            .id(eRole)
                            .name(eRole.getName())
                            .build();
                    return roleRepository.saveAndFlush(role);
                }).collect(Collectors.toList());
    }

}

@Data
class PropJSON {
    private String name;
    private String values;
    private String type;
    private int order;
}

@Data
class BranchJSON {
    private Long id;
    private String name;
    private String mapEmbedUrl;
    private String mapUrl;
    private String address;
    private String phone;
    private String zalo;
    private String manager;
}

@Data
class CustomerJSON {
    private String name;
    private String phone;
    private String address;
}