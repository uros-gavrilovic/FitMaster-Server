package com.mastercode.fitmaster.dataloader;

import com.github.javafaker.Faker;
import com.mastercode.fitmaster.model.MemberEntity;
import com.mastercode.fitmaster.model.MembershipEntity;
import com.mastercode.fitmaster.model.PackageEntity;
import com.mastercode.fitmaster.model.TrainerEntity;
import com.mastercode.fitmaster.model.enums.Gender;
import com.mastercode.fitmaster.repository.MemberRepository;
import com.mastercode.fitmaster.repository.MembershipRepository;
import com.mastercode.fitmaster.repository.PackageRepository;
import com.mastercode.fitmaster.repository.TrainerRepository;
import com.mastercode.fitmaster.util.CustomLogger;
import com.mastercode.fitmaster.util.PatternUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Currency;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
@ConditionalOnProperty(value = "dataloader.init-test-data", havingValue = "true")
public class DataLoader {

    @Autowired
    protected PackageRepository packageRepository;
    @Autowired
    protected MembershipRepository membershipRepository;
    @Autowired
    protected MemberRepository memberRepository;
    @Autowired
    protected TrainerRepository trainerRepository;

    Faker faker = new Faker();

    @PostConstruct
    void loadTestData() {
        CustomLogger.warn("Loading test data ...");

        loadPackages(5);
        loadMembers(25);
        loadTrainers(5);
    }

    private void loadPackages(int counter) {
        for (int i = 0; i < counter; i++) {
            PackageEntity p = new PackageEntity();

            p.setName(faker.lorem().word() + " #" + (i + 1));
            p.setDuration(30);
            p.setPrice(BigDecimal.valueOf(Double.valueOf(faker.commerce().price(20, 100).replace(',', '.'))));
            p.setCurrency(Currency.getInstance("EUR"));

            packageRepository.saveAndFlush(p);
        }
    }

    private void loadMembers(int counter) {
        for (int i = 0; i < counter; i++) {
            MemberEntity m = new MemberEntity();

            m.setFirstName(faker.name().firstName());
            m.setLastName(faker.name().lastName());
            m.setEmail(faker.internet().emailAddress());
            m.setUsername(faker.name().username());
            m.setPassword(new BCryptPasswordEncoder().encode(faker.internet().password(8, 32)));
            m.setGender(Gender.valueOf(faker.demographic().sex().toUpperCase()));
            m.setAddress(faker.address().streetAddress());
            m.setPhoneNumber(faker.regexify(PatternUtils.PHONE_NUMBER_PATTERN));
            m.setBirthDate(LocalDate.ofInstant(faker.date().birthday().toInstant(), ZoneId.systemDefault()));
            m.setIsBanned(i % 8 == 0);
            m.setEmailVerified(i % 5 != 0);

            if (i % 9 != 0) {
                MembershipEntity ms = new MembershipEntity();
                ms.setMemberEntity(m);
                m.getMembershipEntities().add(ms);
                ms.setMembershipPackageEntity(
                        packageRepository.findAll().get(new Random().nextInt((int) packageRepository.count()))
                );
                ms.setStartDate(
                        LocalDate.ofInstant(
                                faker.date().past(30, TimeUnit.DAYS, Date.valueOf(LocalDate.now())).toInstant(),
                                ZoneId.systemDefault()
                        )
                );
                ms.setEndDate(
                        LocalDate.ofInstant(
                            faker.date().future(30, TimeUnit.DAYS, Date.valueOf(LocalDate.now())).toInstant(),
                            ZoneId.systemDefault()
                        )
                );
                membershipRepository.saveAndFlush(ms);
            }

            memberRepository.saveAndFlush(m);
        }
    }

    private void loadTrainers(int counter) {
        for (int i = 0; i < counter; i++) {
            TrainerEntity t = new TrainerEntity();

            t.setFirstName(faker.name().firstName());
            t.setLastName(faker.name().lastName());
            t.setGender(Gender.valueOf(faker.demographic().sex().toUpperCase()));
            t.setAddress(faker.address().streetAddress());
            t.setEmail(faker.internet().emailAddress());
            t.setEmailVerified(true);
            t.setUsername(faker.name().username());
            t.setPassword(new BCryptPasswordEncoder().encode(faker.internet().password(8, 32)));
            t.setPhoneNumber(faker.regexify(PatternUtils.PHONE_NUMBER_PATTERN));
            t.setHireDate(LocalDate.ofInstant(faker.date()
                    .between(Date.valueOf(LocalDate.of(2000, 1, 1)), Date.valueOf(LocalDate.now()))
                    .toInstant(), ZoneId.systemDefault()));

            trainerRepository.saveAndFlush(t);
        }
    }
}
