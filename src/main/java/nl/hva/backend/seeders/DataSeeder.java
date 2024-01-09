package nl.hva.backend.seeders;

import nl.hva.backend.enums.UserRole;
import nl.hva.backend.models.*;
import nl.hva.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.*;

@Component
public class DataSeeder implements CommandLineRunner {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FAQRepository faqRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private ConfigurationRepository configurationRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    private FunctionalityRepository functionalityRepository;

    @Autowired
    private ExpertProjectRatingRepository expertProjectRatingRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RequestUserRepository requestUserRepository;

    @Autowired
    private PasswordEncoder encoder;

    public static String generateRandomString(int length) {
        String candidates = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            sb.append(candidates.charAt(random.nextInt(candidates.length())));
        }

        return sb.toString();
    }

    public static Object getRandomFromGoofyArray(Object... arr) {
        if (arr != null && arr.length > 0) {
            return arr[(int) (Math.random() * arr.length)];
        }

        return null;
    }

    private void createRandomUsers(final int amount, Role role) {
        final String aboutMeDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec vulputate vestibulum arcu, eget consequat mi. Nulla urna libero, interdum eget purus nec, maximus malesuada magna. Nam at quam purus. Aliquam posuere leo in blandit pulvinar. Ut eget accumsan elit. Praesent sed neque pellentesque, fringilla urna vel, ultrices lacus. Donec sed sem vitae libero viverra bibendum ut in purus. In auctor nec neque ac imperdiet.";


        int successes = 0;
        Random random = new Random();
        for (int i = 0; i < amount; i++) {
            User temp = new User(
                    null,
                    generateRandomString(random.nextInt(12) + 12),
                    generateRandomString(6) + " " + generateRandomString(8),
                    i % 2 == 0 ? User.Gender.MALE : User.Gender.FEMALE,
                    LocalDate.of(1990 + random.nextInt(1, 10), random.nextInt(1, 12), random.nextInt(1, 28)),
                    "061234" + random.nextInt(1000, 9999),
                    new Address(
                            "The Netherlands",
                            "Noord-Holland",
                            "Amsterdam",
                            random.nextInt(1000, 9999) + "AB",
                            "Wibautstraat " + random.nextInt(1, 300)
                    ).toString(),
                    User.Sector.values()[random.nextInt(User.Sector.values().length)].name(),
                    User.Client.values()[random.nextInt(User.Client.values().length)].name(),
                    User.Project.values()[random.nextInt(User.Project.values().length)].name(),
                    User.Permit.values()[random.nextInt(User.Permit.values().length)],
                    User.Availability.values()[random.nextInt(User.Availability.values().length)],
                    i % 2 == 0,
                    generateRandomString(random.nextInt(128)),
                    generateRandomString(random.nextInt(8) + 8) + "@gmail.com",
                    null,
                    encoder.encode("welkom123")
//                    encoder.encode(generateRandomString(8))
            );
            temp.getRoles().add(role);
            temp.setAboutMeDescription(aboutMeDescription);

            // check if we didn't fail
            temp = userRepository.save(temp);
            if (temp != null) {
                successes++;
                System.out.printf("%d - %s\n", i + 1, temp);
            }
        }
        System.out.printf("Successfully created %d of %d accounts\n", successes, amount);
    }

    private void addFAQs() {
        FAQ whatIsPeek = new FAQ(
                null,
                "What is Peek?",
                "Peek is a platform that helps you, an expert, find projects that are both challenging and fun.\n" +
                        "We aim to connect experts and clients in order to give everyone a fun and interesting working experience."
        );

        FAQ chat = new FAQ(
                null,
                "How can I interact with others?",
                "It's actually very simple. All you need to get started is open the chat, which is located in the top right of your screen."
        );

        FAQ howDoesContactWork = new FAQ(
                null,
                "How do I get in touch if I would like to get in touch as an (experienced) expert?",
                "Just change your profile so it matches your preferences and our system will contact you, it explains who we are and start the introduction program if there is mutual interest."
        );

        FAQ selection = new FAQ(
                null,
                "How are experts selected for the network, who are eligible?",
                "Experts need to have passion and bring 10+ years of experience to the table; and they need to like working on projects in the network of Peek."
        );

        FAQ collab = new FAQ(
                null,
                "Can I collaborate with other teammates on a project?",
                "Absolutely! Peek encourages collaboration. If a project requires multiple teammates, the platform facilitates team formation, allowing you to collaborate with other skilled professionals."

        );
        FAQ help = new FAQ(
                null,
                "What if I encounter issues with a project?",
                "If you face any challenges during a project, our support team is here to help. You can reach out through the platform, and we'll assist in resolving any issues promptly."

        );
        FAQ quality = new FAQ(
                null,
                "How does Peek ensure the quality of projects?",
                "Peek thoroughly vets each project to ensure it meets quality standards. We prioritize challenging and meaningful opportunities to provide experts with a rewarding experience."

        );
        FAQ pause = new FAQ(
                null,
                "What happens if I need to take a break from accepting projects?",
                "You have the flexibility to control your availability on Peek. If you need to take a break or pause project requests temporarily, you can easily update your profile settings and contact us"

        );

        faqRepository.save(whatIsPeek);
        faqRepository.save(chat);
        faqRepository.save(howDoesContactWork);
        faqRepository.save(selection);
        faqRepository.save(collab);
        faqRepository.save(help);
        faqRepository.save(quality);
        faqRepository.save(pause);
    }

    private void addRandomCommentsForUserByUser(User user, User commenter, int amount) {
        userRepository.save(user);
        userRepository.save(commenter);

        for (int comments = 0; comments < amount; comments++) {
            Comment comment = Comment.createSampleCommentForBy(user, commenter);
            comment = commentRepository.save(comment);
        }
    }

    private void addRandomProjectRatingsForUser(User user, int amount) {
        userRepository.save(user);

        List<Project> projects = projectRepository.findAll();
        Random random = new Random();

        for (int projectRating = 0; projectRating < amount; projectRating++) {
            Project project = projects.get(projectRating);
            ExpertProjectRating expertProjectRating = ExpertProjectRating.createSampleRatingForUserWithProject(user, project);
            expertProjectRating = expertProjectRatingRepository.save(expertProjectRating);
        }
    }

    private void addUsersAndData(Role expertRole, Role brokerRole, Tag... dummyTags) {
        User exp_jd, bkr_mp, bkr_js, exp_jh;
        exp_jd = new User(
                null,
                "john_doe",
                "John Doe",
                User.Gender.MALE,
                LocalDate.of(1969, 2, 16),
                "0612345678",
                new Address(
                        "The Netherlands",
                        "Noord-Holland",
                        "Weesp",
                        "1381XT",
                        "Nieuwstraat 65"
                ).toString(),
                User.Sector.SOFTWARE_DEVELOPMENT.name(),
                User.Client.SMALL_BUSINESSES.name(),
                User.Project.WEB_DEVELOPMENT.name(),
                User.Permit.PERMIT,
                User.Availability.PARTTIME,
                true,
                "No special considerations",
                "john@example.com",
                "john-private@example.com",
                encoder.encode("welkom123")
        );
        exp_jd.getRoles().add(expertRole);
        exp_jd.getTags().add((Tag) getRandomFromGoofyArray(dummyTags));
        addRandomProjectRatingsForUser(exp_jd, 3);

        bkr_mp = new User(
                null,
                "peek",
                "Marius Peek",
                User.Gender.MALE,
                LocalDate.of(1969, 12, 12),
                "0612345678",
                new Address(
                        "The Netherlands",
                        "Groningen",
                        "Groningen",
                        "9712CP",
                        "Broerstraat 5"
                ).toString(),
                User.Sector.NONE.name(),
                User.Client.NONE.name(),
                User.Project.NONE.name(),
                User.Permit.OTHER,
                User.Availability.FULLTIME,
                true,
                "-",
                "mp-tden@example.com",
                null,
                encoder.encode("i_am_the_owner")
        );
        bkr_mp.getRoles().add(brokerRole);
        bkr_mp.getTags().add((Tag) getRandomFromGoofyArray(dummyTags));

        bkr_js = new User(
                null,
                "jane_smith",
                "Jane Smith",
                User.Gender.FEMALE,
                LocalDate.of(1969, 2, 16),
                "0687654321",
                new Address(
                        "The Netherlands",
                        "Gelderland",
                        "Oosterwolde",
                        "8097RD",
                        "Grachtenweg 21"
                ).toString(),
                User.Sector.SOFTWARE_DEVELOPMENT.name(),
                User.Client.SMALL_BUSINESSES.name(),
                User.Project.WEB_DEVELOPMENT.name(),
                User.Permit.DUTCH_NATIONALITY,
                User.Availability.FULLTIME,
                false,
                "No special considerations",
                "jane@example.com",
                "jane-secondmail@example.com",
                encoder.encode("welkom123")
        );
        bkr_js.getRoles().add(brokerRole);
        bkr_js.getTags().add((Tag) getRandomFromGoofyArray(dummyTags));

        exp_jh = new User(
                null,
                "jan_henk",
                "Jan Henk",
                User.Gender.MALE,
                LocalDate.of(1999, 3, 3),
                "0612345678",
                new Address(
                        "The Netherlands",
                        "Noord-Holland",
                        "Weesp",
                        "1381XT",
                        "Nieuwstraat 66"
                ).toString(),
                User.Sector.DATA_ANALYSIS.name(),
                User.Client.ECOMMERCE.name(),
                User.Project.AUGMENTED_VIRTUAL_REALITY.name(),
                User.Permit.DUTCH_NATIONALITY,
                User.Availability.FULLTIME,
                true,
                "No special considerations",
                "janhenk@example.com",
                null,
                encoder.encode("welkom123")
        );
        exp_jh.getRoles().add(expertRole);
        exp_jh.getTags().add((Tag) getRandomFromGoofyArray(dummyTags));
        addRandomCommentsForUserByUser(exp_jh, exp_jd, 10);
        addRandomProjectRatingsForUser(exp_jh, 5);

        // save users into repo
        exp_jd = userRepository.save(exp_jd);
        bkr_mp = userRepository.save(bkr_mp);
        bkr_js = userRepository.save(bkr_js);
        exp_jh = userRepository.save(exp_jh);

        // add an interview to first user
        Interview interview = new Interview(
                null,
                exp_jd.getId(),
                bkr_js.getId(),
                "Interesting title",
                "This is the interview text!",
                LocalDate.now()
        );
        Interview interview2 = new Interview(
                null,
                exp_jd.getId(),
                bkr_js.getId(),
                "Interesting title 2",
                "This is the interview text 2!",
                LocalDate.now()
        );
        Interview interview3 = new Interview(
                null,
                exp_jd.getId(),
                bkr_js.getId(),
                "Interesting title 3",
                "This is the interview text 3!",
                LocalDate.now()
        );

        interviewRepository.save(interview);
        interviewRepository.save(interview2);
        interviewRepository.save(interview3);
    }

    private void addClients(Tag... dummyTags) {
        Client client = new Client(
                null,
                "HvA",
                new Address(
                        "The Netherlands",
                        "Noord-Holland",
                        "Amsterdam",
                        "1234AB",
                        "Wibautstraat 3b"
                ).toString(),
                "abc@example.com",
                "123-456-7890"
        );
        Client client2 = new Client(
                null,
                "XYZ Corporation",
                new Address(
                        "The Netherlands",
                        "Noord-Holland",
                        "Utrecht",
                        "1234AB",
                        "Kaasstraat 15"
                ).toString(),
                "xyz@example.com",
                "987-654-3210"
        );

        clientRepository.save(client);
        clientRepository.save(client2);

        // add test projects
        Project project = new Project(
                null,
                "noord-holland",
                "Project One",
                "2023-11-22",
                "This is the first dummy project",
                "web development",
                "project1.jpg",
                "Content for Project One",
                client,
                new ArrayList<>()
        );

        Project project2 = new Project(
                null,
                "utrecht",
                "Project Two",
                "2023-11-23",
                "This is the second dummy project",
                "mobile development",
                "project2.jpg",
                "Content for Project Two",
                client2,
                new ArrayList<>()
        );

        project.getTags().add((Tag) getRandomFromGoofyArray(dummyTags));
        project2.getTags().add((Tag) getRandomFromGoofyArray(dummyTags));

        projectRepository.save(project);
        projectRepository.save(project2);

        final Random random = new Random();
        final int locations = locationRepository.findAll().size();
        final Location defaultLocation = locationRepository.findById((long) 1).orElseThrow();
        for (int i = 0; i < 10; i++) {
            Project p = new Project(
                    null,
                    locationRepository.findById(random.nextLong(locations)).orElse(defaultLocation).toString(),
                    generateRandomString(16),
                    "",
                    generateRandomString(32),
                    "mobile development",
                    "project2.jpg",
                    generateRandomString(64),
                    client,
                    new ArrayList<>()
            );
            p.getTags().add((Tag) getRandomFromGoofyArray(dummyTags));
            projectRepository.save(p);
        }
    }

    @Override
    public void run(String... args) throws Exception {
        if (!hasSeedingDone()) {
            tagRepository.deleteAll();

            // SETTINGS
            final int randomUsers = 100;

            // add user roles
            Role role = new Role(UserRole.ROLE_EXPERT);
            Role role2 = new Role(UserRole.ROLE_BROKER);
            roleRepository.save(role);
            roleRepository.save(role2);

            Role expertRole = roleRepository.findByName(UserRole.ROLE_EXPERT)
                    .orElseThrow(() -> new RuntimeException("Role not found"));

            Role brokerRole = roleRepository.findByName(UserRole.ROLE_BROKER)
                    .orElseThrow(() -> new RuntimeException("Role not found"));

            // tags
            Tag dummyTag = new Tag(null, "Dummy Tag 1", "primary");
            Tag dummyTag2 = new Tag(null, "Dummy Tag 2", "danger");
            tagRepository.save(dummyTag);
            tagRepository.save(dummyTag2);


            // add test locations
            List<String> locations = Arrays.asList(
                    "Drenthe",
                    "Flevoland",
                    "Friesland",
                    "Gelderland",
                    "Groningen",
                    "Limburg",
                    "Noord-Brabant",
                    "Noord-Holland",
                    "Overijssel",
                    "Utrecht",
                    "Zeeland",
                    "Zuid-Holland"
            );

            for (String location : locations) {
                Location l = new Location(null, location);
                locationRepository.save(l);
            }


            addClients();
            addUsersAndData(expertRole, brokerRole, dummyTag, dummyTag2);
            createRandomUsers(randomUsers, expertRole);
            addFAQs();

            //create notifications
            Notification notification = Notification.createRequestNotification(null);
            Notification notification2 = Notification.createRequestNotification(null);

            notificationRepository.save(notification);
            notificationRepository.save(notification2);


            //add requestUsers
            RequestUser requestUser = new RequestUser(null, "Mehmet", "Dag", "mehmet@gmail.com", false, false, notification);
            RequestUser requestUser2 = new RequestUser(null, "Serkan", "Yilmaz", "serkan@gmail.com", false, false, notification2);

            requestUserRepository.save(requestUser);
            requestUserRepository.save(requestUser2);

            // other
            List<String> skills = Arrays.asList(
                    "Java", "Python", "JavaScript", "ReactJS", "Angular", "Node.js", "HTML", "CSS",
                    "PHP", "Laravel", "Spring Framework", "Express.js", "MongoDB", "SQL", "AWS",
                    "Docker", "Kubernetes", "Git", "Agile Methodologies", "Remote Work"
            );

            List<String> colors = Arrays.asList(
                    "primary", "secondary", "success", "danger", "warning", "info", "light", "dark"
            );

            for (String skill : skills) {
                String randomColor = getRandomColor(colors);
                Tag tag = new Tag(null, skill, randomColor);
                tagRepository.save(tag);
            }

            List<String> functionalities = Arrays.asList(
                    "Frontend Development", "Backend Development", "Mobile Development", "DevOps",
                    "Data Science", "Artificial Intelligence", "Cybersecurity", "Internet of Things", "Cloud Computing"
            );

            for (String functionality : functionalities) {
                Functionality f = new Functionality(null, functionality);
                functionalityRepository.save(f);
            }

            // set the flag indicating that seeding is done
            setSeedingDoneFlag();
        }
    }

    private static String getRandomColor(List<String> colors) {
        Random rand = new Random();
        return colors.get(rand.nextInt(colors.size()));
    }

    private boolean hasSeedingDone() {
        return configurationRepository.existsBySeedingDone(true);
    }

    private void setSeedingDoneFlag() {
        // Set the seeding_done flag in the configuration table
        Configuration config = configurationRepository.findByConfigName("seeding_done");
        if (config == null) {
            config = new Configuration(null, "seeding_done", true);
        } else {
            config.setValue(true);
        }
        configurationRepository.save(config);
    }
}
