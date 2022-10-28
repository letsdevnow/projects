package CodeSharingE.controller;

import CodeSharingE.common.*;
import CodeSharingE.exception.*;
import CodeSharingE.crud.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class CodeSharingController {
    private final SnippetRepository snippetRepository;

    CodeSharingController(SnippetRepository snippetRepository) {
        this.snippetRepository = snippetRepository;
    }

    //get json with snippet by ID
    @GetMapping("/api/code/{snippetID}")
    public Snippet getCodeObj(@PathVariable int snippetID) {
        if (!hasSnippetById(snippetID)) {
            throw new SnippetNotFoundException("Snippet not found, ID: " + snippetID);
        } else {
            return getSnippetById(snippetID);
        }
    }

    //post new snippet
    @PostMapping(value = "/api/code/new", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> newCode(@RequestBody NewSnippet newSnippet) {
        Snippet snippet = new Snippet();
        snippet.setCode(newSnippet.getCode());
        snippet.setDate(LocalDateTime.now());
        Snippet savedSnippet = snippetRepository.save(snippet);

        return new ResponseEntity<String>(String.format("{\"id\": \"%d\"}", savedSnippet.getId()), HttpStatus.OK);
    }

    //get json with 10 latest snippets
    @GetMapping("/api/code/latest")
    public List<Snippet> getSnippets() {
        return getLatestSnippets();
    }

    //return page with snippet by ID
    @GetMapping(value = "/code/{snippetID}", produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getSnippetPage(@PathVariable int snippetID) {
        if (!hasSnippetById(snippetID)) {
            throw new SnippetNotFoundException("Snippet not found, ID: " + snippetID);
        } else {
            ModelAndView model = new ModelAndView();
            model.setViewName("main");
            Snippet snippet = getSnippetById(snippetID);
            model.addObject("code", snippet.getCode());
            model.addObject("date", snippet.getDate());
            return model;
        }
    }

    //return page with new snippet form
    @GetMapping(value = "/code/new", produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getNewCodePage() {
        ModelAndView model = new ModelAndView();
        model.setViewName("new");
        return model;
    }

    //return page with 10 latest snippets
    @GetMapping("/code/latest")
    public ModelAndView getSnippetsPage() {
        ModelAndView model = new ModelAndView();
        model.setViewName("list");
        model.addObject("snippets", getLatestSnippets());
        return model;
    }

    //return list with 10 latest snippets
    private List<Snippet> getLatestSnippets() {

        return snippetRepository.findLatestSnippets();
/*        Iterable<Snippet> iterableSnippetsFromDb = snippetRepository.findAll();
        List<Snippet> snippetsFromDb = new ArrayList<>();
        iterableSnippetsFromDb.forEach(snippetsFromDb::add);

        return snippetsFromDb.stream()
                .skip(Math.max(0, snippetsFromDb.size() - 10))
                .sorted(new SnippetComparator())
                .collect(Collectors.toList());*/
    }

    //check whether in DB there is the snippet by ID
    private boolean hasSnippetById(int Id) {
        return snippetRepository.existsById(Id);
    }

    //return snippet by ID
    private Snippet getSnippetById(int Id) {
        Optional<Snippet> snippet = snippetRepository.findById(Id);
        return snippet.orElse(new Snippet());
    }
}
