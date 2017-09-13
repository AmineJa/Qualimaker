package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.apache.commons.io.IOUtils;
import org.qualimaker.domain.DocumentInterne;

import org.qualimaker.repository.DocumentInterneRepository;
import org.qualimaker.repository.search.DocumentInterneSearchRepository;
import org.qualimaker.web.rest.util.HeaderUtil;
import org.qualimaker.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.springframework.util.StreamUtils.BUFFER_SIZE;

/**
 * REST controller for managing DocumentInterne.
 */
@RestController
@RequestMapping("/api")
public class DocumentInterneResource {

    private final Logger log = LoggerFactory.getLogger(DocumentInterneResource.class);

    private static final String ENTITY_NAME = "documentInterne";

    private final DocumentInterneRepository documentInterneRepository;

    private final DocumentInterneSearchRepository documentInterneSearchRepository;

    public DocumentInterneResource(DocumentInterneRepository documentInterneRepository, DocumentInterneSearchRepository documentInterneSearchRepository) {
        this.documentInterneRepository = documentInterneRepository;
        this.documentInterneSearchRepository = documentInterneSearchRepository;
    }

    /**
     * POST  /document-internes : Create a new documentInterne.
     *
     * @param documentInterne the documentInterne to create
     * @return the ResponseEntity with status 201 (Created) and with body the new documentInterne, or with status 400 (Bad Request) if the documentInterne has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/document-internes")
    @Timed
    public ResponseEntity<DocumentInterne> createDocumentInterne(@Valid @RequestBody DocumentInterne documentInterne) throws URISyntaxException {
        log.debug("REST request to save DocumentInterne : {}", documentInterne);
        if (documentInterne.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new documentInterne cannot already have an ID")).body(null);
        }
        DocumentInterne result = documentInterneRepository.save(documentInterne);
        documentInterneSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/document-internes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /document-internes : Updates an existing documentInterne.
     *
     * @param documentInterne the documentInterne to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated documentInterne,
     * or with status 400 (Bad Request) if the documentInterne is not valid,
     * or with status 500 (Internal Server Error) if the documentInterne couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/document-internes")
    @Timed
    public ResponseEntity<DocumentInterne> updateDocumentInterne(@Valid @RequestBody DocumentInterne documentInterne) throws URISyntaxException {
        log.debug("REST request to update DocumentInterne : {}", documentInterne);
        if (documentInterne.getId() == null) {
            return createDocumentInterne(documentInterne);
        }
        DocumentInterne result = documentInterneRepository.save(documentInterne);
        documentInterneSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, documentInterne.getId().toString()))
            .body(result);
    }

    /**
     * GET  /document-internes : get all the documentInternes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of documentInternes in body
     */
    @GetMapping("/document-internes")
    @Timed
    public ResponseEntity<List<DocumentInterne>> getAllDocumentInternes(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of DocumentInternes");
        Page<DocumentInterne> page = documentInterneRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/document-internes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


  /*  @GetMapping("/download.do")
    public void doDownload(HttpServletRequest request,HttpServletResponse response) throws IOException {
        DocumentInterne doc = documentInterneRepository.findOne(pageable);

        // get absolute path of the application
        ServletContext context = request.getServletContext();
        String appPath = context.getRealPath("");

        System.out.println("appPath = " + appPath);

        // construct the complete absolute path of the file
        String fullPath = appPath + filePath;
        File downloadFile = new File(fullPath);
        FileInputStream inputStream = new FileInputStream(downloadFile);

        // get MIME type of the file
        String mimeType = context.getMimeType(fullPath);
        if (mimeType == null) {
            // set to binary type if MIME mapping not found
            mimeType = "application/octet-stream";
        }
        System.out.println("MIME type: " + mimeType);

        // set content attributes for the response
        response.setContentType(mimeType);
        response.setContentLength((int) downloadFile.length());

        // set headers for the response
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
            downloadFile.getName());
        response.setHeader(headerKey, headerValue);

        // get output stream of the response
        OutputStream outStream = response.getOutputStream();

        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = -1;

        // write bytes read from the input stream into the output stream
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }

        inputStream.close();
        outStream.close();

    }
*/

    /*@RequestMapping("/download/{documentId}")
    public String download(@PathVariable("documentId")Long documentId, HttpServletResponse response) {

        DocumentInterne doc = documentInterneRepository.findOne(documentId);
        try {
            response.setHeader("Content-Disposition", "inline;filename=\"" +doc.getFichierContentType()+ "\"");
            OutputStream out = response.getOutputStream();
            response.setContentType(doc.getFichierContentType());
            ByteArrayInputStream in = new ByteArrayInputStream(doc.getFichier());
            IOUtils.copy(in, out);
            out.flush();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @RequestMapping(value = { "/download-document-{docId}" }, method = RequestMethod.GET)
    public String downloadDocument(@PathVariable int userId, @PathVariable long docId, HttpServletResponse response) throws IOException {
        DocumentInterne document = documentInterneRepository.findOne(docId);
        response.setContentType(document.getFichierContentType());
        response.setHeader("Content-Disposition","attachment; filename=\"" + document.getCode() +"\"");

        FileCopyUtils.copy(document.getFichier(), response.getOutputStream());

        return "redirect:/document-interne?page&sort&search'";
    }*/

    /**
     * GET  /document-internes/:id : get the "id" documentInterne.
     *
     * @param id the id of the documentInterne to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the documentInterne, or with status 404 (Not Found)
     */
    @GetMapping("/document-internes/{id}")
    @Timed
    public ResponseEntity<DocumentInterne> getDocumentInterne(@PathVariable Long id) {
        log.debug("REST request to get DocumentInterne : {}", id);
        DocumentInterne documentInterne = documentInterneRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(documentInterne));
    }

    /**
     * DELETE  /document-internes/:id : delete the "id" documentInterne.
     *
     * @param id the id of the documentInterne to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/document-internes/{id}")
    @Timed
    public ResponseEntity<Void> deleteDocumentInterne(@PathVariable Long id) {
        log.debug("REST request to delete DocumentInterne : {}", id);
        documentInterneRepository.delete(id);
        documentInterneSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/document-internes?query=:query : search for the documentInterne corresponding
     * to the query.
     *
     * @param query the query of the documentInterne search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/document-internes")
    @Timed
    public ResponseEntity<List<DocumentInterne>> searchDocumentInternes(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of DocumentInternes for query {}", query);
        Page<DocumentInterne> page = documentInterneSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/document-internes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
