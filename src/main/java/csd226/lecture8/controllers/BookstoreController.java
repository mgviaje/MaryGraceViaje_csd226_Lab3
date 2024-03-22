package csd226.lecture8.controllers;

import csd226.lecture8.data.Registry;
import csd226.lecture8.repositories.RegistryRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BookstoreController {



    //    @GetMapping("/staffcontent")
//    public ResponseEntity<String> getStaffContent(){ // map a URL to a method
//        return ResponseEntity.ok("getStaffContent() : Staff");
//    }
//    @GetMapping("/publiccontent")
//    public ResponseEntity<String> getPublicContent(){ // map a URL to a method
//        return ResponseEntity.ok("getPublicContent() : Home");
//    }
    @GetMapping("/about")
    public ResponseEntity<String> getAbout(){ // map a URL to a method
        return ResponseEntity.ok("getAbout() : About");
    }
//    @GetMapping("/protectedPage")
//    public ResponseEntity<String> getProtectedContent(){ // map a URL to a method
//        return ResponseEntity.ok("Protected Content");
//    }
//    @GetMapping("/publiccontent2")
//    public Content getPublicContent2(){ // map a URL to a method
//        return new Content("some content");
//    }


    // save data
    @PutMapping("/publiccontent")
    public ResponseEntity<Boolean> savePublicContent(@RequestBody @Valid Registry content) {
        Boolean result = updateRegistry(content.getRegistryKey(), content.getRegistryValue());
        return ResponseEntity.ok(result);
    }

    @PutMapping("/staffcontent")
    public ResponseEntity<Boolean> saveStaffContent(@RequestBody @Valid Registry content) {
        Boolean result = updateRegistry(content.getRegistryKey(), content.getRegistryValue());
        return ResponseEntity.ok(result);
    }

    // get data

    @GetMapping("/publiccontent")
    public ResponseEntity<String> getPublicContent() {
        return ResponseEntity.ok(getRegistry("public_content"));
    }

    @GetMapping("/staffcontent")
    public ResponseEntity<String> getStaffContent() {
        return ResponseEntity.ok(getRegistry("staff_content"));
    }





    @Autowired
    RegistryRepository registryRepository;

    private Boolean updateRegistry(String registryKey, String registryValue) {
        //Find the record for the registry entry based on the supplied key
        List<Registry> registryEntries = registryRepository.findByRegistryKey(registryKey);

        Registry registryEntry = new Registry();

        if (registryEntries.size() == 0) {
            registryEntry.setRegistryKey(registryKey);
        } else {
            registryEntry = registryEntries.get(0);
        }

        registryEntry.setRegistryValue(registryValue);

        //Update the registry table with new value
        registryRepository.save(registryEntry);

        return true;
    }

    private String getRegistry(String registryKey) {
        //Find the record for the registry entry based on the supplied key
        List<Registry> registryEntries = registryRepository.findByRegistryKey(registryKey);

        Registry registryEntry = new Registry();

        if (registryEntries.size() == 0) {
            return "";
        }

        return registryEntries.get(0).getRegistryValue();
    }

}
