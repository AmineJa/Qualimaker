package org.qualimaker.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(org.qualimaker.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.PersistentToken.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.User.class.getName() + ".persistentTokens", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Formation.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Formation.class.getName() + ".employes", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Abscence.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Conge.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Critereevaluation.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.DemandeFormation.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Employe.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Employe.class.getName() + ".conges", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Employe.class.getName() + ".formations", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Employe.class.getName() + ".fonctions", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Evaluation.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.EvaluationFormation.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Fichierjoint.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Formateur.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Formateur.class.getName() + ".typeformateurs", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Carriere.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.FormationComp.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Groupe.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Integre.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Jour.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.NatureAbs.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Natureformation.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Programme.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Serviice.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Sites.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.TypeConge.class.getName(), jcacheConfiguration);

            cm.createCache(org.qualimaker.domain.EtatDemande.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.EtatDemande.class.getName() + ".demandeFormations", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Formation.class.getName() + ".events", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Abscence.class.getName() + ".employes", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Serviice.class.getName() + ".postes", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Poste.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Naturediscipline.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Discipline.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Employe.class.getName() + ".integres", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Employe.class.getName() + ".respints", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Servicepost.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Servicepost.class.getName() + ".postes", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Poste.class.getName() + ".serviceposts", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Serviice.class.getName() + ".serviceposts", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Servicepost.class.getName() + ".serviices", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Servicepost.class.getName() + ".sites", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Calendrier.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Calendrier.class.getName() + ".jours", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Jour.class.getName() + ".calendriers", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Events.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Origine.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.DocumenExterne.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.TypeDocumentation.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.DocumenExterne.class.getName() + ".employes", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.TypeDocumentation.class.getName() + ".documenExternes", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Employe.class.getName() + ".documenExternes", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.LieuxClassement.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.DroitaccesDocument.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.DroitaccesDocument.class.getName() + ".employes", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Employe.class.getName() + ".droitaccesDocuments", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.DocumentInterne.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.DocumentInterne.class.getName() + ".droitaccesDocumentResources", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.DocumentInterne.class.getName() + ".redacteurs", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.DocumentInterne.class.getName() + ".verificateurs", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.DocumentInterne.class.getName() + ".approuvateurs", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.DroitaccesDocument.class.getName() + ".redroles", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.DroitaccesDocument.class.getName() + ".verifroles", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.DroitaccesDocument.class.getName() + ".approles", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Sites.class.getName() + ".documentInternes", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Processus.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Processus.class.getName() + ".documentInternes", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.TypeDocumentation.class.getName() + ".documentInternes", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.TypeDocumentation.class.getName() + ".typeDocumentations", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Notification.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Enregistrement.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Profilsfonction.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Profilsfonction.class.getName() + ".fonctions", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Fonction.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Remplacer.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Employe.class.getName() + ".remplacers", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Employe.class.getName() + ".remplacernvs", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Remplacer.class.getName() + ".profilesfonctions", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Remplacer.class.getName() + ".profilsfonctions", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Profilsfonction.class.getName() + ".remplacers", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Fonction.class.getName() + ".profilsfonctions", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Employe.class.getName() + ".documentintsups", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.DroitaccesDocument.class.getName() + ".superviseurdroits", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.DroitaccesDocument.class.getName() + ".verificateurdroits", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.DroitaccesDocument.class.getName() + ".redacteurdroits", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.DroitaccesDocument.class.getName() + ".approubateurdroits", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.DocumentInterne.class.getName() + ".droitaccesDocuments", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Employe.class.getName() + ".carrieres", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.TypeContrat.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.TypeContrat.class.getName() + ".carrieres", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Competences.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Employe.class.getName() + ".competences", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.DroitaccesDocument.class.getName() + ".verificateurs", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.DroitaccesDocument.class.getName() + ".redacteurs", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.DroitaccesDocument.class.getName() + ".approubateurs", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.DocumentInterne.class.getName() + ".approubateurs", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.DroitaccesDocument.class.getName() + ".superviseurs", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Employe.class.getName() + ".enregistrements", jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
