package com.chutneytesting.design.api.plugins.jira;

import com.chutneytesting.design.domain.plugins.jira.JiraRepository;
import com.chutneytesting.design.domain.plugins.jira.JiraTargetConfiguration;
import com.chutneytesting.execution.domain.jira.JiraXrayPlugin;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(JiraModuleController.BASE_URL)
@CrossOrigin(origins = "*")
public class JiraModuleController {

    public static final String BASE_URL = "/api/ui/jira/v1/";
    public static final String BASE_SCENARIO_URL = "scenario";
    public static final String BASE_CAMPAIGN_URL = "campaign";
    public static final String BASE_TEST_EXEC_URL = "testexec";
    public static final String BASE_CONFIGURATION_URL = "configuration";

    private final JiraRepository jiraRepository;
    private final JiraXrayPlugin jiraXrayPlugin;

    public JiraModuleController(JiraRepository jiraRepository, JiraXrayPlugin jiraXrayPlugin) {
        this.jiraRepository = jiraRepository;
        this.jiraXrayPlugin = jiraXrayPlugin;
    }

    @GetMapping(path = BASE_SCENARIO_URL, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map<String, String> getLinkedScenarios() {
        return jiraRepository.getAllLinkedScenarios();
    }

    @GetMapping(path = BASE_CAMPAIGN_URL, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map<String, String> getLinkedCampaigns() {
        return jiraRepository.getAllLinkedCampaigns();
    }

    @GetMapping(path = BASE_SCENARIO_URL + "/{scenarioId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JiraDto getByScenarioId(@PathVariable String scenarioId) {
        String jiraId = jiraRepository.getByScenarioId(scenarioId);
        return ImmutableJiraDto.builder()
            .id(jiraId)
            .chutneyId(scenarioId)
            .build();
    }

    @PostMapping(path = BASE_SCENARIO_URL,
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JiraDto saveForScenario(@RequestBody JiraDto jiraDto) {
        jiraRepository.saveForScenario(jiraDto.chutneyId(), jiraDto.id());
        return ImmutableJiraDto.builder()
            .id(jiraDto.id())
            .chutneyId(jiraDto.chutneyId())
            .build();
    }

    @DeleteMapping(path = BASE_SCENARIO_URL + "/{scenarioId}")
    public void removeForScenario(@PathVariable String scenarioId) {
        jiraRepository.removeForScenario(scenarioId);
    }


    @GetMapping(path = BASE_CAMPAIGN_URL + "/{campaignId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JiraDto getByCampaignId(@PathVariable String campaignId) {
        String jiraId = jiraRepository.getByCampaignId(campaignId);
        return ImmutableJiraDto.builder()
            .id(jiraId)
            .chutneyId(campaignId)
            .build();
    }

    @GetMapping(path = BASE_TEST_EXEC_URL + "/{testExecId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<String> getScenariosByCampaignIg(@PathVariable String testExecId) {
        if(testExecId.isEmpty())
            return new ArrayList<>();

        Map<String, String> allLinkedScenarios = jiraRepository.getAllLinkedScenarios();
        List<String> testExecScenariosId = jiraXrayPlugin.getTestExecutionScenarios(testExecId);

        return allLinkedScenarios.entrySet()
            .stream()
            .filter(entry -> testExecScenariosId.contains(entry.getValue()))
            .map(entry ->entry.getKey()
            ).collect(Collectors.toList());
    }

    @PostMapping(path = BASE_CAMPAIGN_URL,
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JiraDto saveForCampaign(@RequestBody JiraDto jiraDto) {
        jiraRepository.saveForCampaign(jiraDto.chutneyId(), jiraDto.id());
        return ImmutableJiraDto.builder()
            .id(jiraDto.id())
            .chutneyId(jiraDto.chutneyId())
            .build();
    }

    @DeleteMapping(path = BASE_CAMPAIGN_URL + "/{campaignId}")
    public void removeForCampaign(@PathVariable String campaignId) {
        jiraRepository.removeForCampaign(campaignId);
    }

    @GetMapping(path = BASE_CONFIGURATION_URL, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JiraConfigurationDto getConfiguration() {
        JiraTargetConfiguration jiraTargetConfiguration = jiraRepository.loadServerConfiguration();
        return ImmutableJiraConfigurationDto.builder()
            .url(jiraTargetConfiguration.url)
            .username(jiraTargetConfiguration.username)
            .password(jiraTargetConfiguration.password)
            .build();
    }

    @PostMapping(path = BASE_CONFIGURATION_URL,
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void saveConfiguration(@RequestBody JiraConfigurationDto jiraConfigurationDto) {
        jiraRepository.saveServerConfiguration(new JiraTargetConfiguration(jiraConfigurationDto.url(), jiraConfigurationDto.username(), jiraConfigurationDto.password()));
    }

}
