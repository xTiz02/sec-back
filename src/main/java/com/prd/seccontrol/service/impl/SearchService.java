package com.prd.seccontrol.service.impl;

import com.prd.seccontrol.service.inter.SearchCriteria;
import com.prd.seccontrol.util.SEConstants;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.ManagedType;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Service;

@Service
public class SearchService<T> {

  @PersistenceContext
  private EntityManager entityManager;

  private String TICKET_CLASS = "Ticket";

  private Map<String, List<String>> specialClassesAndPropertiesForLike =
      new HashMap<String, List<String>>() {{
        put(TICKET_CLASS, Arrays.asList("documentSeries", "customer.idDocumentNumber"));
      }};

  public Page<T> search(String queryFromUrl, Pageable pageable, Class<T> clazz) {
    if(queryFromUrl != null && queryFromUrl.endsWith(",")){
      queryFromUrl = queryFromUrl.substring(0, queryFromUrl.length() - 1);
    }
    List<SearchCriteria> params = createParams(queryFromUrl);

    final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    final CriteriaQuery<T> query = builder.createQuery(clazz);
    final Root<T> root = query.from(clazz);

    Predicate predicate = builder.conjunction();

    for (final SearchCriteria param : params) {
      // find the attribute Path required by criteriaBuilder
      // since it can be a root or nested attribute we delegate this to findAttributePath()
      Path attributePath = findAttributePath(param.getKey(), root);
      // we evaluate which kind of statement should we perform
      // operation = >
      if (param.getOperation().equalsIgnoreCase(">")) {
        if (attributePath.getJavaType() == Date.class) {
          DateFormat format = new SimpleDateFormat(SEConstants.DATE_FORMAT);
          Date date;
          try {
            date = format.parse(param.getValue().toString());
          } catch (ParseException e) {
            throw new RuntimeException("Invalid date.");
          }
          predicate = builder.and(predicate,
              builder.greaterThanOrEqualTo(attributePath, date));
        } else {
          predicate = builder.and(predicate,
              builder.greaterThanOrEqualTo(attributePath, param.getValue().toString()));
        }
        // operation = <
      } else if (param.getOperation().equalsIgnoreCase("<")) {
        if (attributePath.getJavaType() == Date.class) {
          DateFormat format = new SimpleDateFormat(SEConstants.DATE_FORMAT);
          Date date;
          try {
            date = format.parse(param.getValue().toString());
          } catch (ParseException e) {
            throw new RuntimeException("Invalid date.");
          }
          predicate = builder.and(predicate,
              builder.lessThanOrEqualTo(attributePath, date));
        } else {
          predicate = builder.and(predicate,
              builder.lessThanOrEqualTo(attributePath, param.getValue().toString()));
        }
        // operation = !:
      } else if (param.getOperation().equalsIgnoreCase("!:")) {
        if (param.getValue().toString().equals("Ø")) {
          // Si el valor es Ø (null), usamos isNotNull
          predicate = builder.and(predicate, builder.isNotNull(attributePath));
        } else if (attributePath.getJavaType() == String.class) {
          predicate = builder.and(predicate,
              builder.notEqual(attributePath, param.getValue().toString()));
        } else if (attributePath.getJavaType() == boolean.class
            || attributePath.getJavaType() == Boolean.class) {
          predicate = builder.and(predicate, builder.notEqual(attributePath,
              Boolean.parseBoolean(param.getValue().toString())));
        } else if (attributePath.getJavaType() == Date.class) {
          DateFormat format = new SimpleDateFormat(SEConstants.DATE_FORMAT);
          try {
            Date date = format.parse(param.getValue().toString());
//            date = DateUtils.addHours(date, 5);
            predicate = builder.and(predicate, builder.notEqual(attributePath, date));
          } catch (ParseException e) {
            throw new RuntimeException("Invalid date.");
          }
        } else if (Enum.class.isAssignableFrom(attributePath.getJavaType())) {
          Class<? extends Enum> enumClass = (Class<? extends Enum>) attributePath.getJavaType();
          EnumSet<? extends Enum> enumValues = EnumSet.allOf(enumClass);
          Enum<?> enumValue = Enum.valueOf(enumClass, param.getValue().toString());

          if (enumValues.contains(enumValue)) {
            predicate = builder.and(predicate, builder.notEqual(attributePath, enumValue));
          } else {
            throw new RuntimeException("Invalid ENUM Search Value");
          }
        } else {
          predicate = builder.and(predicate, builder.notEqual(attributePath, param.getValue()));
        }
        // operation = ..
      } else if (param.getOperation().equalsIgnoreCase("$")) {
        String[] values = param.getValue().toString().split(",");
        if (values.length != 2) {
          throw new RuntimeException(
              "Invalid 'between' value format. Expected two values separated by a comma.");
        }

        if (attributePath.getJavaType() == Date.class) {
          DateFormat format = new SimpleDateFormat(SEConstants.DATE_FORMAT);
          format.setLenient(false);

          TimeZone limaTz = TimeZone.getTimeZone(ZoneId.of(SEConstants.PERU_LOCAL_TIMEZONE));
          format.setTimeZone(limaTz);

          Date fromDate;
          Date toDate;
          try {
            fromDate = format.parse(values[0].trim());
            toDate   = format.parse(values[1].trim());
          } catch (ParseException e) {
            throw new RuntimeException("Invalid date format for 'between' operation.");
          }

          Calendar cal = Calendar.getInstance(limaTz);

          cal.setTime(fromDate);
          cal.set(Calendar.HOUR_OF_DAY, 0);
          cal.set(Calendar.MINUTE, 0);
          cal.set(Calendar.SECOND, 0);
          cal.set(Calendar.MILLISECOND, 0);
          fromDate = cal.getTime();

          cal.setTime(toDate);
          cal.set(Calendar.HOUR_OF_DAY, 23);
          cal.set(Calendar.MINUTE, 59);
          cal.set(Calendar.SECOND, 59);
          cal.set(Calendar.MILLISECOND, 999);
          toDate = cal.getTime();

          predicate = builder.and(predicate, builder.between(attributePath, fromDate, toDate));

        } else if (attributePath.getJavaType() == String.class) {
          predicate = builder.and(predicate,
              builder.between(attributePath, values[0].trim(), values[1].trim()));

        }else if (Number.class.isAssignableFrom(attributePath.getJavaType())) {

          try {
            BigDecimal from = new BigDecimal(values[0].trim());
            BigDecimal to   = new BigDecimal(values[1].trim());

            @SuppressWarnings("unchecked")
            Path<BigDecimal> numberPath = (Path<BigDecimal>) attributePath;

            predicate = builder.and(
                predicate,
                builder.between(numberPath, from, to)
            );
          } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid numeric value for 'between' operation.");
          }

        } else {
          throw new RuntimeException(
              "The 'between' operation is only supported for Date and String types.");
        }
        // operation = :
      } else if (param.getOperation().equalsIgnoreCase(":")) {
        // if value is @ we need to do an OR with isNull
        if (param.getValue().toString().equals("@")) {
          predicate = builder.or(predicate, builder.isNull(attributePath));
        } else if (param.getValue().toString().contains("|")) {
          // if operation contains | means we should perform an IN statement
          if (attributePath.getJavaType() == long.class ||
              attributePath.getJavaType() == Long.class) {
            // TODO: improve IN criteria to support different data types
            List<String> values = Arrays.asList(param.getValue().toString().split("\\|"));
            In<Long> in = builder.in(attributePath);
            values.forEach(i -> in.value(Long.parseLong(i)));
            predicate = builder.and(predicate, in);
          } else if (Enum.class.isAssignableFrom(attributePath.getJavaType()) || attributePath.getJavaType()
              .isEnum()) {
            predicate = createPredicateForMultipleEnums(predicate, param, builder,
                attributePath);
          }
          // if attribute is String we perform a like statement
        } else if (attributePath.getJavaType() == String.class) {

          predicate = createPredicateForLikeStatement(predicate, clazz, param, builder,
              attributePath);

          // if attribute is boolean we need to parse before the equal statement
        } else if (attributePath.getJavaType() == boolean.class
            || attributePath.getJavaType() == Boolean.class) {
          predicate = builder.and(predicate, builder.equal(attributePath,
              Boolean.parseBoolean(param.getValue().toString())));
          // if attribute is date we need to parse the incoming value
          // since the front end is sending a string following SIVConstants.DATE_FORMAT
        } else if (attributePath.getJavaType() == Date.class) {
          DateFormat format = new SimpleDateFormat(SEConstants.DATE_FORMAT);
          Date fromDate;
          Date toDate;
          try {
            fromDate = format.parse(param.getValue().toString());
          } catch (ParseException e) {
            throw new RuntimeException("Invalid date.");
          }

          fromDate = Date.from(Instant.now().plus(5, ChronoUnit.HOURS));
          toDate = Date.from(Instant.now().plus(24, ChronoUnit.HOURS));

          predicate = builder.and(predicate,
              builder.greaterThanOrEqualTo(attributePath, fromDate));

          predicate = builder.and(predicate, builder.lessThan(attributePath, toDate));
          // Ø means null
        } else if (param.getValue().toString().equals("Ø")) {

          predicate = builder.and(predicate, builder.isNull(attributePath));

        }
        // !Ø means not null
        else if (param.getValue().toString().equals("!Ø")) {

          predicate = builder.and(predicate, builder.isNotNull(attributePath));

        }
        // deal with enums
        else if (Enum.class.isAssignableFrom(attributePath.getJavaType())) {
          // Check if the class is an enum
          Class<? extends Enum> enumClass = (Class<? extends Enum>) attributePath.getJavaType();

          // Get the enum values
          EnumSet<? extends Enum> enumValues = EnumSet.allOf(enumClass);

          // Convert the string value to the enum
          Enum<?> enumValue = Enum.valueOf(enumClass, param.getValue().toString());

          // Check if the enum value is in the set of enum values
          if (enumValues.contains(enumValue)) {
            predicate = builder.and(predicate, builder.equal(attributePath, enumValue));
          } else {
            throw new RuntimeException("Invalid ENUM Search Value");
          }
        }
        // by default the operation : results in a equal statement
        else {

          predicate = builder.and(predicate, builder.equal(attributePath, param.getValue()));

        }
      }
    }
    query.where(predicate);

    // convert to page using pageable
    int pageNumber = pageable.getPageNumber();
    int pageSize = pageable.getPageSize();

    final CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
    countQuery.select(builder.count(countQuery.from(clazz)));
    final Long count = entityManager.createQuery(countQuery).getSingleResult();
    // set order by
    query.orderBy(QueryUtils.toOrders(pageable.getSort(), root, builder));
    // set the correct page
    TypedQuery<T> typedQuery;
    typedQuery = entityManager.createQuery(query);
    typedQuery.setFirstResult(pageNumber * pageSize);
    typedQuery.setMaxResults(pageSize);

    // create page to return
    List<T> results = typedQuery.getResultList();
    return new PageImpl<>(results, pageable, count);
  }

  private Predicate createPredicateForLikeStatement(Predicate predicate, Class clazz,
      SearchCriteria param, CriteriaBuilder builder, Path attributePath) {

    // 1. Check if class and properties require special like
    for (Map.Entry<String, List<String>> entry : specialClassesAndPropertiesForLike.entrySet()) {

      if (entry.getKey().equals(clazz.getSimpleName())) {

        for (String property : entry.getValue()) {

          if (param.getKey().equals(property)) {

            predicate = builder.and(predicate, builder.like(attributePath, param.getValue() + "%"));

            return predicate;

          }
        }
      }
    }

    // 2. Otherwise do a normal like
    predicate = builder.and(predicate,
        builder.like(attributePath, "%" + param.getValue() + "%"));

    return predicate;

  }

  private Predicate createPredicateForMultipleEnums(Predicate predicate,
      SearchCriteria param, CriteriaBuilder builder, Path attributePath) {
    // Check if the class is an enum
    Class<? extends Enum> enumClass = (Class<? extends Enum>) attributePath.getJavaType();
    // Get the enum values
    EnumSet<? extends Enum> enumValues = EnumSet.allOf(enumClass);
    // Support for multiple enum values separated
    String[] rawValues = param.getValue().toString().split("\\|");
    In<Enum<?>> in = builder.in(attributePath);
    for (String raw : rawValues) {
      String trimmed = raw.trim();
      try {
        // Convert the string value to the enum
        Enum<?> enumValue = Enum.valueOf(enumClass, trimmed);
        // Check if the enum value is in the set of enum values
        if (!enumValues.contains(enumValue)) {
          throw new RuntimeException("Invalid ENUM Search Value: " + trimmed);
        }
        in.value(enumValue);
      } catch (IllegalArgumentException ex) {
        throw new RuntimeException("Invalid ENUM Search Value: " + trimmed);
      }
    }
    predicate = builder.and(predicate, in);

    return predicate;
  }

  private Path<?> findAttributePath(String attributesString, Root<T> root) {
    if (attributesString.contains(".")) {
      // Split the attribute path into segments
      String[] attributes = attributesString.split("\\.");

      // Start with the root
      Path<?> path = root;
      From<?, ?> currentFrom = root;

      // Navigate through each segment
      for (String attribute : attributes) {
        if (currentFrom != null) {
          // Try to determine if this is a join able path before attempting
          boolean shouldJoin = false;

          try {
            // This block is a safer way to determine if an attribute is join able
            // First check if we've already joined this attribute
            boolean alreadyJoined = currentFrom.getJoins().stream()
                .anyMatch(join -> join.getAttribute().getName().equals(attribute));

            if (!alreadyJoined) {
              // If using Hibernate, we can check the mapping type
              if (currentFrom instanceof Root) {
                ManagedType<?> managedType = ((Root<?>) currentFrom).getModel();
                if (managedType.getAttribute(attribute).isCollection() ||
                    managedType.getAttribute(attribute).isAssociation()) {
                  shouldJoin = true;
                }
              } else {
                // For Join, we need to check if the joined type has this attribute
                // and if it's a collection or association
                Join<?, ?> join = (Join<?, ?>) currentFrom;
                ManagedType<?> joinType = null;

                // Try to get the managed type from the join
                try {
                  if (join.getModel() instanceof ManagedType) {
                    joinType = (ManagedType<?>) join.getModel();
                  }
                } catch (Exception e) {
                  // Fall back to get() if we can't determine the type
                }

                if (joinType != null && joinType.getAttribute(attribute) != null) {
                  Attribute<?, ?> attr = joinType.getAttribute(attribute);
                  shouldJoin = attr.isCollection() || attr.isAssociation();
                }
              }
            } else {
              // We already have this join, reuse it
              Optional<? extends Join<?, ?>> existingJoin = currentFrom.getJoins().stream()
                  .filter(join -> join.getAttribute().getName().equals(attribute))
                  .findFirst();

              if (existingJoin.isPresent()) {
                path = existingJoin.get();
                currentFrom = existingJoin.get();
                continue; // Skip to next attribute since we've handled this one
              }
            }
          } catch (IllegalArgumentException e) {
            // If any exception occurs when checking, assume it's not join able
          }

          if (shouldJoin) {
            try {
              // If we determined it should be a join, attempt the join
              Join<?, ?> join = currentFrom.join(attribute, JoinType.LEFT);
              path = join;
              currentFrom = join;
            } catch (Exception e) {
              // If join fails for any reason, fall back to get()
              path = path.get(attribute);
              currentFrom = null; // Can't join further
            }
          } else {
            // Use get() for basic attributes
            path = path.get(attribute);
            currentFrom = null; // Can't join on basic attributes
          }
        } else {
          // We're no longer on a From, just use get()
          assert path != null;
          path = path.get(attribute);
          // Still can't join further
        }
      }

      return path;
    }

    // Simple case: just return the direct attribute
    return root.get(attributesString);
  }

  public List<SearchCriteria> createParams(String query) {

    // will match the three parts of a query, where key and value are the first and last parentheses
    // groups, respectively, consisting of one or more optional word characters, and the operator is
    // one of the characters inside the square brackets in the middle parentheses group
    Pattern QUERY_PATTERN =
        Pattern.compile("([\\w\\.]*\\w+)(!:|[:<>$])([\\w\\-\\s\\/\\|\\!Ø\\(\\),]*),");

    List<SearchCriteria> params = new ArrayList<>();

    Matcher matcher = QUERY_PATTERN.matcher(query + ",");

    // one-based indexes for the parts of the query inside the query pattern
    final int KEY_INDEX = 1;
    final int OPERATOR_INDEX = 2;
    final int VALUE_INDEX = 3;

    while (matcher.find()) {
      params.add(new SearchCriteria(matcher.group(KEY_INDEX), matcher.group(OPERATOR_INDEX),
          matcher.group(VALUE_INDEX)));
    }

    return params;
  }

  public Map<String, String> getKeyValueMap(String query) {

    Pattern QUERY_PATTERN = Pattern.compile("([\\w\\.]*\\w+)([:<>])([\\w-\\s\\/\\|\\!Ø\\(\\)]*),");

    Matcher matcher = QUERY_PATTERN.matcher(query + ",");

    final int KEY_INDEX = 1;
    final int VALUE_INDEX = 3;

    Map<String, String> keyValueMap = new HashMap<>();

    while (matcher.find()) {

      keyValueMap.put(matcher.group(KEY_INDEX), matcher.group(VALUE_INDEX));

    }

    return keyValueMap;

  }

}
