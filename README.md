# Dziennik elektroniczny
## Cele i zakres systemu

 
1. Program dziennika elektronicznego ma służyć prostej organizacji uczniów i ich ocen w systemie. Nauczyciel jak i rodzic mają stały wgląd w kształcenie Swoich wychowanków. Rozwiązujemy problem skomplikowanych systemów w których rodzic starszego pokolenia nie wie gdzie i w jaki sposób sprawdzić oceny jak i uwagi kierowane w stronę ucznia. Przy realizacji projektu kierujemy się intuicyjnością co pozwoli każdemu bez problemów obsłużyć nasz produkt. 
 

2. Potencjalnymi grupami odbiorów projektu będą szkoły co za tym idzie nauczyciele, dyrektorowie, uczniowie, jak i rodzice dzieci które kształcą się w szkołach z naszym systemem.

3. W organizacji gromadzone są dane dotyczące uczniów, ich ocen jak i uwag. Prawo do wprowadzania tych danych ma dyrektor jak i nauczyciele. 

4. Generowane dokumenty bedą dotyczyć ocen i danych uczniów. Dokument w formacie pdf w zależności od prawie dostępu będzie zawierał różne dane.

- Dla ucznia jak i dla rodzica możliwość wygenerowania własnych ocen z systemu w postaci dokumentu pdf 

- Dla nauczyciela możliwość wygenerowania ocen i informacji całych grup do których został przydzielony.

- Dla dyrektora możliwość wygenerowania ocen i informacji na temat wszystkich grup dostępnych w placówce.*

5. Dyrektor posiadający wszystkie dane o wszystkich użytkownikach rozdziela grupy uczniów danym nauczycielom dając im wgląd w informację tylko o swoich wychowankach. Rodzic otrzymuję wgląd w informację tylko na temat własnego dziecka. W przypadku wprowadzenia oceny przez nauczyciela informacja o tym zostaje udostępniona dyrektorowi jak i rodzicom konkretnego użytkownika.

6. - Dyrektor ma prawo zmienić oceny i dane użytkownika
   - Nauczyciel ma prawo zmienić oceny użytkownika
   - Rodzic ma prawo tylko do odczytu danych w systemie. 
    (W razie chęci zmiany danych ucznia np. miejsca zamieszkania. Konieczne jest poinformowanie dyrektora placówki)

	
## Funkcjonalności systemu:
### Nauczyciel:
- wyświetlanie listy uczniów
- dodawanie ocen
- dodawanie uwag
- sprawdzanie obecności
- akceptacja usprawiedliwienia

### Uczeń:
- Wyświetlanie ocen
- Wyświetlanie nieobecności
- Wyświetlanie uwag

### Rodzic:
- wpisywanie usprawiedliwień
- Wyświetlanie nieobecności
- Wyświetlanie uwag
- Wyświetlanie ocen

### Dyrektor:
- dodawanie uczniów i nauczycieli oraz przydzielanie ich do klas

## Diagramy UML:
### Diagram klas
![Diagram klas](https://github.com/mjochab/PZ_2019_Lab2_Gr4/blob/master/Diagramy/klas.jpg)
### Diagram przypadków użycia
![Diagram przypadków użycia](https://github.com/mjochab/PZ_2019_Lab2_Gr4/blob/master/Diagramy/przypadki.png)
### Diagram sekwencji
![Diagram sekwencji](https://github.com/mjochab/PZ_2019_Lab2_Gr4/blob/master/Diagramy/Diagram_sekwencji.png)
### Diagram aktywności
![Diagram aktywności](https://github.com/mjochab/PZ_2019_Lab2_Gr4/blob/master/Diagramy/Diagram_aktywnosci.png)

## Instrukcja instalacji aplikacji:

- użyć dołączonego instalatora i wybrać folder w którym zainstalować dziennik elektroniczny. Konieczne zarezerwowanie ~600MB miejsca na dysku. Instalator automatycznie dodaje baze danych do autostartu, zatem przy każdym uruchomieniu dziennika elektronicznego będzie także uruchamiana baza danych mysql.

## Obsługa aplikacji:

### Pierwsze uruchomienie:
Użyć pliku .exe, po kilku minutach ładowania ukaże się okno logowania.
By dziennik elektroniczny działał poprawnie, należy w pierwszej kolejności zalogować się na konto dyrektora - domyślny login i hasło:
dyrektor/pass
Kolejno należy stworzyć plan zajęć, przedmioty. Dodać uczniów i ich rodziców, przydzielić uczniów do klas. Uprzednio należy także utworzyć dla każdego użytkownika (rodzic/uczen/nauczyciel) login i hasło by mógł się zalogować.

Przewodnik po aplikacji:
1. Ekran logowania. Podać login i hasło.
![logowanie](https://github.com/mjochab/PZ_2019_Lab2_Gr4/blob/Login_v2/Projekty%20okienek/logowanie.PNG)

2. Okno ucznia:
Po zalogowaniu jest widoczna tabela z przedmiotami i ocenami wraz z typem oceny (np. spr), dostępna jest też zakładka - nieobecności, gdzie uczeń może sprawdzić stan swoich obecności.
Ostatnią zakładką jest plan zajęć.


3. Okno rodzica:
Po zalogowaniu należy wybrać dziecko, którego oceny chcemy wyświetlić. Oceny wyświetlają się tak samo jak w oknie uczeń. 
W zakładce nieobecności możemy usprawiedliwić nieobecności wybranego dziecka.
Rodzic może także zobaczyć plan zajeć swoich dzieci w zakładce plan zajeć.

4. Okno nauczyciela.
Po zalogowaniu mamy do wyboru klasy których uczy dany nauczyciel, a także zakładkę z wychowankami jeśli takowy nauczyciel ich posiada.
Wybieramy klasę którą chcemy obejrzeć i przechodzimy dalej.
Po wybraniu klasy ukaże się tabela z ocenami z danego przedmiotu, możemy wybrać przedmiot z którego oceny chcemy zobaczyć. Nauczyciel ma możliwość dodawania, edycji i usuwania ocen.
Kolejną możliwością jest sprawdzanie i zmiana stanu obecności danych uczniów. Należy kliknąć na zakładkę Nieobecności, ukaże się tabela z przedmiotami które uczymy i datami w których nauczamy danego przedmiotu. Jako nauczyciel możemy wstawić obecność i nieobecność.

4. Okno dyrektora:







Tutoriale:

- Hibernate Criteria (selecty) https://www.mkyong.com/hibernate/hibernate-criteria-examples/

- Hibernate Criteria 409. http://docs.jboss.org/hibernate/orm/5.4/userguide/html_single/Hibernate_User_Guide.html#fetching

