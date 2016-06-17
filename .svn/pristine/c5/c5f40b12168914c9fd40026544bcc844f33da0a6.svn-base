package com.skl.cloud.common.xml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AbstractTypeHierarchyTraversingFilter;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.oxm.UncategorizedMappingException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

class Jaxb2TypeScanner {

	private static final String RESOURCE_PATTERN = "/**/*.class";

	private static final TypeFilter[] JAXB2_TYPE_FILTERS = new TypeFilter[] {
			new AnnotationTypeFilter(XmlRootElement.class, false),
			new AnnotationTypeFilter(XmlType.class, false),
			new AnnotationTypeFilter(XmlSeeAlso.class, false),
			new AnnotationTypeFilter(XmlEnum.class, false),
			new AnnotationTypeFilter(XmlRegistry.class, false)
	};

	private final ResourcePatternResolver resourcePatternResolver;

	private final String[] packagesToScan;
	
	private TypeFilter[] assignableTypeFilters;


	public Jaxb2TypeScanner(ClassLoader classLoader, Class<?>[] assignableClassesToScan, String... packagesToScan) {
		Assert.notEmpty(packagesToScan, "'packagesToScan' must not be empty");
		this.resourcePatternResolver = new PathMatchingResourcePatternResolver(classLoader);
		this.packagesToScan = packagesToScan;
		if(assignableClassesToScan != null) {
			assignableTypeFilters = new Jaxb2AssignableTypeFilter[assignableClassesToScan.length];
			for (int i = 0; i < assignableClassesToScan.length; i++) {
				assignableTypeFilters[i] = new Jaxb2AssignableTypeFilter(assignableClassesToScan[i]);
			}
		}
	}


	/**
	 * Scan the packages for classes marked with JAXB2 annotations.
	 * @throws UncategorizedMappingException in case of errors
	 */
	public Class<?>[] scanPackages() throws UncategorizedMappingException {
		try {
			List<Class<?>> jaxb2Classes = new ArrayList<Class<?>>();
			for (String packageToScan : this.packagesToScan) {
				String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
						ClassUtils.convertClassNameToResourcePath(packageToScan) + RESOURCE_PATTERN;
				Resource[] resources = this.resourcePatternResolver.getResources(pattern);
				MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);
				for (Resource resource : resources) {
					MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
					if (isJaxb2Class(metadataReader, metadataReaderFactory)) {
						String className = metadataReader.getClassMetadata().getClassName();
						Class<?> jaxb2AnnotatedClass = this.resourcePatternResolver.getClassLoader().loadClass(className);
						jaxb2Classes.add(jaxb2AnnotatedClass);
					}
				}
			}
			return jaxb2Classes.toArray(new Class<?>[jaxb2Classes.size()]);
		}
		catch (IOException ex) {
			throw new UncategorizedMappingException("Failed to scan classpath for unlisted classes", ex);
		}
		catch (ClassNotFoundException ex) {
			throw new UncategorizedMappingException("Failed to load annotated classes from classpath", ex);
		}
	}

	protected boolean isJaxb2Class(MetadataReader reader, MetadataReaderFactory factory) throws IOException {
		for (TypeFilter filter : JAXB2_TYPE_FILTERS) {
			if (filter.match(reader, factory)) {
				return true;
			}
		}
		if(assignableTypeFilters != null) {
			for (TypeFilter filter : assignableTypeFilters) {
				if(filter.match(reader, factory)) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	private static class Jaxb2AssignableTypeFilter  extends AbstractTypeHierarchyTraversingFilter {

		private final Class<?> targetType;


		/**
		 * Create a new AssignableTypeFilter for the given type.
		 * @param targetType the type to match
		 */
		public Jaxb2AssignableTypeFilter(Class<?> targetType) {
			super(true, true);
			this.targetType = targetType;
		}


		@Override
		public boolean match(MetadataReader reader,
				MetadataReaderFactory factory) throws IOException {
			ClassMetadata classMetadata = reader.getClassMetadata();
			if(classMetadata.isAbstract() || classMetadata.isInterface()) {
				return false;
			}
			return super.match(reader, factory);
		}



		@Override
		protected boolean matchClassName(String className) {
			return this.targetType.getName().equals(className);
		}

		@Override
		protected Boolean matchSuperClass(String superClassName) {
			return matchTargetType(superClassName);
		}

		@Override
		protected Boolean matchInterface(String interfaceName) {
			return matchTargetType(interfaceName);
		}

		protected Boolean matchTargetType(String typeName) {
			if (this.targetType.getName().equals(typeName)) {
				return true;
			}
			else if (Object.class.getName().equals(typeName)) {
				return false;
			}
			else if (typeName.startsWith("java")) {
				try {
					Class<?> clazz = ClassUtils.forName(typeName, getClass().getClassLoader());
					return this.targetType.isAssignableFrom(clazz);
				}
				catch (Throwable ex) {
					// Class not regularly loadable - can't determine a match that way.
				}
			}
			return null;
		}

} 

}

