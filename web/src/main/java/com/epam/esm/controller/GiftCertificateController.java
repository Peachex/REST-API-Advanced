package com.epam.esm.controller;

import com.epam.esm.attribute.ResponseAttribute;
import com.epam.esm.dto.GiftCertificate;
import com.epam.esm.hateoas.Hateoas;
import com.epam.esm.response.OperationResponse;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/certificates")
public class GiftCertificateController {
    private final GiftCertificateService<GiftCertificate> service;
    private final Hateoas<GiftCertificate> certificateHateoas;
    private final Hateoas<OperationResponse> responseHateoas;

    @Autowired
    public GiftCertificateController(GiftCertificateService<GiftCertificate> service, Hateoas<GiftCertificate>
            certificateHateoas, @Qualifier("certificateOperationResponseHateoas") Hateoas<OperationResponse>
                                             responseHateoas) {
        this.service = service;
        this.certificateHateoas = certificateHateoas;
        this.responseHateoas = responseHateoas;
    }

    @GetMapping("/all")
    public List<GiftCertificate> findAllGiftCertificates(@RequestParam int page, @RequestParam int elements) {
        List<GiftCertificate> giftCertificates = service.findAll(page, elements);
        giftCertificates.forEach(certificateHateoas::createHateoas);
        return giftCertificates;
    }

    @GetMapping
    public List<GiftCertificate> findCertificatesWithTags(@RequestParam int page, @RequestParam int elements,
                                                          @RequestParam(required = false) String tagName,
                                                          @RequestParam(required = false) String certificateName,
                                                          @RequestParam(required = false) String certificateDescription,
                                                          @RequestParam(required = false) String sortByName,
                                                          @RequestParam(required = false) String sortByDate) {
        List<GiftCertificate> giftCertificates = service.findCertificatesWithTagsByCriteria(page, elements, tagName,
                certificateName, certificateDescription, sortByName, sortByDate);
        giftCertificates.forEach(certificateHateoas::createHateoas);
        return giftCertificates;
    }

    @PostMapping("/new")
    public OperationResponse createGiftCertificate(@RequestBody GiftCertificate giftCertificate) {
        OperationResponse response = new OperationResponse(OperationResponse.Operation.CREATION,
                ResponseAttribute.CERTIFICATE_CREATE_OPERATION, String.valueOf(service.insert(giftCertificate)));
        responseHateoas.createHateoas(response);
        return response;
    }

    @GetMapping("/{id}")
    public GiftCertificate findCertificateById(@PathVariable String id) {
        GiftCertificate giftCertificate = service.findById(id);
        certificateHateoas.createHateoas(giftCertificate);
        return giftCertificate;
    }

    @DeleteMapping("/{id}")
    public OperationResponse deleteGiftCertificate(@PathVariable String id) {
        service.delete(id);
        OperationResponse response = new OperationResponse(OperationResponse.Operation.DELETION,
                ResponseAttribute.CERTIFICATE_DELETE_OPERATION, id);
        responseHateoas.createHateoas(response);
        return response;
    }

    @PatchMapping("/{id}")
    public OperationResponse updateGiftCertificate(@PathVariable String id,
                                                   @RequestBody GiftCertificate giftCertificate) {
        service.update(id, giftCertificate);
        OperationResponse response = new OperationResponse(OperationResponse.Operation.UPDATE,
                ResponseAttribute.CERTIFICATE_UPDATE_OPERATION, id);
        responseHateoas.createHateoas(response);
        return response;
    }
}
