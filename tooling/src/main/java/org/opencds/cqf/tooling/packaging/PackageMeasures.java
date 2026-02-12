package org.opencds.cqf.tooling.packaging;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import org.opencds.cqf.tooling.packaging.r4.PackageMeasure;
import org.opencds.cqf.tooling.utilities.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

public class PackageMeasures {

    private static final Logger logger = LoggerFactory.getLogger(PackageMeasures.class);

    public PackageMeasures(String igRoot, FhirContext fhirContext, boolean includeDependencies, boolean includeTerminology, boolean includeTests, String fhirServerUrl) {
        this(igRoot, fhirContext, includeDependencies, includeTerminology, includeTests, fhirServerUrl, null);
    }

    public PackageMeasures(String igRoot, FhirContext fhirContext, boolean includeDependencies, boolean includeTerminology, boolean includeTests, String fhirServerUrl, String measureToPackagePath) {
        var measureResourcePaths = IOUtils.getMeasurePaths(fhirContext);
        if (fhirContext.getVersion().getVersion() == FhirVersionEnum.R4) {
            var filteredPaths = filterMeasurePaths(measureResourcePaths, measureToPackagePath);
            if (filteredPaths.isEmpty()) {
                logger.warn("No Measure resources matched the path: {}", measureToPackagePath);
            } else {
                filteredPaths.forEach(
                        path -> new PackageMeasure(igRoot, fhirContext, path, includeDependencies, includeTerminology, includeTests, fhirServerUrl)
                                .packageArtifact());
            }
        } else {
            throw new UnsupportedOperationException("Package operation for Measure resources is not supported for FHIR version: " + fhirContext.getVersion().getVersion().getFhirVersionString());
        }
    }

    private Collection<String> filterMeasurePaths(Collection<String> measureResourcePaths, String measureToPackagePath) {
        if (measureToPackagePath == null || measureToPackagePath.isEmpty()) {
            return measureResourcePaths;
        }

        try {
            var targetFile = new File(measureToPackagePath).getCanonicalFile();

            if (targetFile.isDirectory()) {
                var targetDir = targetFile.getCanonicalPath();
                return measureResourcePaths.stream()
                        .filter(path -> {
                            try {
                                return new File(path).getCanonicalPath().startsWith(targetDir + File.separator)
                                        || new File(path).getCanonicalPath().equals(targetDir);
                            } catch (IOException e) {
                                return false;
                            }
                        })
                        .collect(Collectors.toList());
            } else {
                var targetPath = targetFile.getCanonicalPath();
                return measureResourcePaths.stream()
                        .filter(path -> {
                            try {
                                return new File(path).getCanonicalPath().equals(targetPath);
                            } catch (IOException e) {
                                return false;
                            }
                        })
                        .collect(Collectors.toList());
            }
        } catch (IOException e) {
            logger.warn("Error resolving measure path: {}. Packaging all Measures.", e.getMessage());
            return measureResourcePaths;
        }
    }

}
