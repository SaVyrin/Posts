package ru.surf.gallery.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.surf.gallery.data.database.*
import ru.surf.gallery.domain.UserRepository
import ru.surf.gallery.utils.AuthInputValidator
import ru.surf.gallery.utils.toDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.random.Random

@HiltViewModel
class LoginViewModel @Inject constructor(
    val userDao: UserDao,
    val postDao: PostDao,
    @Named("vsu_user") private val userRepository: UserRepository
) : ViewModel() {

    private val mutableLoginStatus = MutableLiveData(LoginStatus.NOT_LOGGED_IN)
    val loginStatus: LiveData<LoginStatus> = mutableLoginStatus

    private val mutableLoginFieldStatus = MutableLiveData<LoginFieldStatus>()
    val loginFieldStatus: LiveData<LoginFieldStatus> = mutableLoginFieldStatus

    private val mutablePasswordFieldStatus = MutableLiveData<PasswordFieldStatus>()
    val passwordFieldStatus: LiveData<PasswordFieldStatus> = mutablePasswordFieldStatus

    private var login = ""
    private var password = ""

    fun initVSUDB() {
        viewModelScope.launch {
            userRepository.clearUserData()
            for (userId in 1..1000) {
                val phoneNumber = 70000000000 + userId
                userDao.insert(
                    User(
                        userId.toString(),
                        "+$phoneNumber",
                        "user-$userId@post.ru"
                    )
                )
            }
            val calendar3 = Calendar.getInstance();
            calendar3.set(Calendar.DAY_OF_MONTH, 3);
            val currentDate3 = calendar3.timeInMillis;
            val calendar5 = Calendar.getInstance();
            calendar5.set(Calendar.DAY_OF_MONTH, 5);
            val currentDate5 = calendar5.timeInMillis;
            val calendar7 = Calendar.getInstance();
            calendar7.set(Calendar.DAY_OF_MONTH, 7);
            val currentDate7 = calendar7.timeInMillis;
            postDao.insertAll(
                listOf(
                    Post(
                        "2",
                        "Дневник Келли",
                        "Дорогой дневник. Сегодня тот самый день, когда можно смело забить на уроки и тусоваться с друзьями до самого утра. Этот день недели принято называть пятницей. По традиции ученики старшей школы устраивают вечеринку на озере, на которую обязаны идти все, уважающие себя, старшеклассники. Пропустить такое веселье способны только мальчики и девочки, которые слишком сильно зависят от родителей, которые предпочитают вместо вечеринок прочитать книги, и которые лучше выпьют сладкий чай перед сном, чем пиво",
                        "https://i.pinimg.com/736x/c9/e5/97/c9e59738b505fe8948c87b9cfe033900.jpg",
                        currentDate3.toDateFormat()
                    ),
                    Post(
                        "3",
                        "Чашечка свежего кофе",
                        "Для бариста и посетителей кофеен специальные кружки для кофе — это ещё один способ проконтролировать вкус напитка и приготовить его именно так, как нравится вам.\r\n\r\nТеперь, кроме регулировки экстракции, настройки помола, времени заваривания и многого что помогает выделять нужные характеристики кофе, вы сможете выбрать и кружку для кофе в зависимости от сорта.",
                        "https://i.pinimg.com/750x/a8/e6/af/a8e6af0802d24e9b4e8988493b246980.jpg",
                        currentDate5.toDateFormat()
                    ),
                    Post(
                        "4",
                        "Поездка в Шотландию",
                        "Роскошь и величие – главные синонимы этой поездки. Пассажиров приветствуют красной дорожкой, волынкой в исполнении настоящих шотландцев и, конечно, шампанским в смотровом вагоне, сделанном еще в 1908 году. Однодневные или семидневные маршруты обычно отправляются из Лондона и Эдинбурга. По пути вы увидите разные замки, дома, сады и музеи, а также проедете через знаменитые Северные высоты (Northern Highlands).",
                        "https://i.pinimg.com/originals/a3/f6/9a/a3f69a12fd7889336601a803d23e5672.jpg",
                        currentDate3.toDateFormat()
                    ),
                    Post(
                        "5",
                        "Милый корги",
                        "Отважная, жизнерадостная и изобретательная собака, достойная британской короны.\r\n\r\nКорги – это воплощенный оптимизм и жизнелюбие. Неунывающие «коржики» сумели найти множество плюсов в своей необычной внешности и стать неиссякаемым источником удовольствия для хозяев.\r\n\r\nСейчас качества корги используются на пастбищах и в поисково-спасательных операциях. Единственное, чего корги не могут, – это работать охранниками: для этого они слишком добры! Сверхспособность породы – отменное чувство юмора. Корги не прочь как пошутить, так и посмеяться над хорошей шуткой. Добавьте к этому бодрость, жизнелюбие и оптимизм, и вы не сможете удержаться и не завести еще одного – самого последнего! – мохнатого «коржика»",
                        "https://i.pinimg.com/736x/69/cf/58/69cf58bae5c5c0ba9f48ad6e7a3c99bc.jpg",
                        currentDate7.toDateFormat()
                    ),
                    Post(
                        "6",
                        "Путешествие в Исландию",
                        "Blue Lagoon находится прям около аэропорта. Все обзоры называют цену в 50 евро за её посещение, но умалчивают, что та половина, где не купаются — доступна совершенно бесплатно.\r\n\r\nКрасивых мест так много, что на второй день просто устаёшь останавливаться каждый раз и развиваешь скилл фотографирования прямо на ходу.",
                        "https://i.vas3k.ru/6l0.jpg",
                        currentDate3.toDateFormat()
                    ),
                    Post(
                        "7",
                        "Закат",
                        "Закаты и восходы – одно из самых притягательных явлений природы, а в Юго-Восточной Азии ещё и одно из самых стремительных. Торопливое солнце показывается над горизонтом, каждый раз разыгрывая уникальный театр форм и красок.\r\n\r\nКазалось бы, кого можно удивить закатом? Но нет, это зрелище уникально каждый раз и по своему прекрасно во всех уголках планеты.\r\n\r\nНа Пангане закаты прекрасны и в общем, на этом можно поставить точку)\r\nНо если продолжить, то стоит отметить, что практически вся западная часть острова, это отличная смотровая площадка на которой медитируя можно наблюдать за путешествием огненного шара",
                        "https://loveyouplanet.com/wp-content/uploads/2017/04/zakatyi-na-pangane-55.jpg",
                        currentDate7.toDateFormat()
                    ),
                    Post(
                        "8",
                        "Завтрак",
                        "Завтрак — важный прием пищи. Он запускает все механизмы, дает необходимую энергию, а вкусная и яркая еда задает настроение на весь день, — достаточно весомые аргументы, чтобы поесть утром.\r\n\r\nБольше пользы от завтрака будет, когда он проведен в приятной компании за обсуждением чего-то интересного. Это отличный повод иногда выбираться на встречу с друзьями или завтракать вместе с семьей, обсуждая планы на день и сочетание вкусов приготовленного. Так мы получаем двойной заряд хорошим настроением на целый день!",
                        "https://admin.justfood.pro/Content/Images/Blog/29865033-6465-4302-9422-3798aad3fc99.jpg",
                        currentDate5.toDateFormat()
                    ),
                    Post(
                        "9",
                        "Весна в Петербурге",
                        "Весной в Питере погода не устойчивая, в марте дуют промозглые северные ветры. То снег, то дождь не дают людям долго оставаться на улице. Только к середине апреля начинает пригревать солнце, термометр показывает плюсовую температуру. В это время в Санкт-Петербурге еще не так много туристов, в кафе есть свободные места, а жилье стоит в два и даже три раза дешевле, чем во время высокого сезона. Хороший вариант для бюджетного отдыха.\r\n\r\nВ апреле, всего две-три недели в году, в городе пахнет свежими огурцами – идет сезон корюшки, маленькой, но особенной для петербуржцев рыбки. Это своеобразный бренд города, который притягивает любителей гастрономических удовольствий.\r\n\r\nВ мае повышается температура, газоны зеленеют, расцветают растения и улицы заполняются туристами. Конец весны богат на городские события и праздники",
                        "https://st2.depositphotos.com/3980029/11492/i/600/depositphotos_114927316-stock-photo-panoramic-view-of-saint-petersburg.jpg",
                        currentDate5.toDateFormat()
                    )
                )
            )

            for (userId in 1..1000) {
                val postsCount = (2..9).random()
                for (postId in 1..postsCount) {
                    postDao.addToUserPosts(
                        UserPost(
                            userId.toString(),
                            postId.toString()
                        )
                    )
                }
            }
        }
    }

    fun setLogin(newLoginValue: String) {
        login = newLoginValue
    }

    fun setPassword(newPasswordValue: String) {
        password = newPasswordValue
    }

    fun logInUser() {
        if (validateInputs()) {
            viewModelScope.launch {
                try {
                    mutableLoginStatus.value = LoginStatus.IN_PROGRESS
                    val newLoginStatus = userRepository.login(login, password)
                    mutableLoginStatus.value = newLoginStatus
                } catch (error: Throwable) {
                    error.printStackTrace()
                    mutableLoginStatus.value = LoginStatus.ERROR_INTERNET
                }
            }
        }
    }

    private fun validateInputs(): Boolean {
        val loginValidity = AuthInputValidator.getLoginValidity(login)
        val passwordValidity = AuthInputValidator.getPasswordValidity(password)
        mutableLoginFieldStatus.value = loginValidity
        mutablePasswordFieldStatus.value = passwordValidity

        return loginValidity == LoginFieldStatus.VALID
    }
}